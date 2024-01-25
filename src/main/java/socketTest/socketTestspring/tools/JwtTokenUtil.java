package socketTest.socketTestspring.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import socketTest.socketTestspring.dto.TokenDto;
import socketTest.socketTestspring.service.RefreshTokenService;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final RefreshTokenService refreshTokenService;

    //token claim key
    private static final String MEMBER = "memberId";

    //token secret key
    @Value("${jwt.token.secret.access}")
    private String accessSecretKey;
    @Value("${jwt.token.secret.refresh}")
    private String refreshSecretKey;

    //token secret key spec
    private SecretKeySpec accessSecretKeySpec;
    private SecretKeySpec refreshSecretKeySpec;

    @PostConstruct
    protected void init() {
        accessSecretKeySpec = new SecretKeySpec(accessSecretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        refreshSecretKeySpec = new SecretKeySpec(refreshSecretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }
    public String createToken(String memberId, TokenType type) {
        SecretKeySpec secretKeySpec = getSecretKeySpec(type);
        long now = System.currentTimeMillis();
        long expireTime = type.getExpireTime();
        return Jwts.builder()
                .claim(MEMBER, memberId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expireTime))
                .signWith(secretKeySpec)
                .compact();
    }
    public TokenDto createAllToken(String memberId){
        return new TokenDto(createToken(memberId, TokenType.ACCESS), createToken(memberId, TokenType.REFRESH));
    }

    private SecretKeySpec getSecretKeySpec(TokenType type) {
        if(type.equals(TokenType.ACCESS)) return accessSecretKeySpec;
        return refreshSecretKeySpec;
    }
    public Boolean refreshTokenValidation(String memberId, String token) throws UsernameNotFoundException {
        return token.equals(refreshTokenService.findOne(memberId).getRefreshToken());
    }
    public String getMemberId(String token, TokenType type) throws ExpiredJwtException {
        SecretKeySpec secretKeySpec = getSecretKeySpec(type);
        try{
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(secretKeySpec).build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get(MEMBER).toString();
        } catch(ExpiredJwtException e) {
            log.error("expired Jwt token. Need to refresh");
            throw e;
        } catch (Exception e) {
            log.error("Cannot find any member with this token");
            return null;
        }
    }
}

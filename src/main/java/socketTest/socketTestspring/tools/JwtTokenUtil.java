package socketTest.socketTestspring.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import socketTest.socketTestspring.domain.RefreshToken;
import socketTest.socketTestspring.dto.TokenDto;
import socketTest.socketTestspring.repository.RefreshTokenRepository;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final RefreshTokenRepository refreshTokenRepository;
    public static final long EXPIRE_TIME = 60 * 60 * 1000; //1시간
    public static final long REFRESH_EXPIRE_TIME = 24 * 60 * 60 * 1000; //24시간
    //token claim key
    private static final String MEMBER = "memberId";
    public static final String ACCESS_TOKEN = "Access_Token";
    public static final String REFRESH_TOKEN = "Refresh_Token";

    //token secret key
    @Value("${jwt.token.secret.access}")
    private String secretKey;
    @Value("${jwt.token.secret.refresh}")
    private String refreshSecretKey;

    //token secret key spec
    private SecretKeySpec secretKeySpec;
    private SecretKeySpec refreshSecretKeySpec;

    @PostConstruct
    protected void init() {
        secretKeySpec = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        refreshSecretKeySpec = new SecretKeySpec(refreshSecretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }
    public String getHeaderToken(HttpServletRequest request, String type){
        return type.equals("Access") ? request.getHeader(ACCESS_TOKEN) : request.getHeader(REFRESH_TOKEN);
    }
    public TokenDto createAllToken(String memberId){
        return new TokenDto(createAccessToken(memberId),createRefreshToken(memberId));
    }
    public String createAccessToken(String memberId){
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claim(MEMBER, memberId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRE_TIME))
                .signWith(secretKeySpec)
                .compact();
    }
    public String createRefreshToken(String memberId){
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claim(MEMBER,memberId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now+REFRESH_EXPIRE_TIME))
                .signWith(refreshSecretKeySpec)
                .compact();
    }

    public String getMemberId(String token, String type) throws ExpiredJwtException {
        SecretKeySpec secretKey  = type.equals("Access") ? secretKeySpec : refreshSecretKeySpec;
        try{
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(secretKey).build()
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
    public Boolean refreshTokenValidation(String refreshToken1) throws ExpiredJwtException {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberId(getMemberId(refreshToken1, "Refresh"));
        return refreshToken.isPresent() && refreshToken1.equals(refreshToken.get().getRefreshToken());
    }
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("Refresh_Token", refreshToken);
    }
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Access_Token", accessToken);
    }
}

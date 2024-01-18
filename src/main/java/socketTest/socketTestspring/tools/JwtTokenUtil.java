package socketTest.socketTestspring.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import socketTest.socketTestspring.exception.myExceptions.classes.InvalidJwtException;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {

    //token expiration time
    public static final long EXPIRE_TIME = 60 * 60 * 1000; //1시간
    public static final long REFRESH_EXPIRE_TIME = 24 * 60 * 60 * 1000; //24시간
    private static final long REFRESH_TIME = 24 * 60 * 60 * 1000 / 4; // 6시간

    //token claim key
    private static final String MEMBER = "memberId";

    //token secret key
    @Value("${jwt.token.secret}")
    private String secretKey;
    @Value("${jwt.token.refresh}")
    private String refreshSecretKey;
    private SecretKeySpec secretKeySpec;
    private SecretKeySpec refreshSecretKeySpec;

    protected void init() {
        secretKeySpec = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        refreshSecretKeySpec = new SecretKeySpec(refreshSecretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }

    public String createToken(String memberId){
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .claim(MEMBER, memberId)
                .setIssuedAt(new Date(now))//현재 시간
                .setExpiration(new Date(now + EXPIRE_TIME))//현재 시간 + 유효 시간 = 만료 시간
                .signWith(secretKeySpec)
                .compact();
    }

    public String getMemberId(String token) throws InvalidJwtException {
        try{
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(secretKeySpec).build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get(MEMBER).toString();
        } catch (Exception e) {
            throw new InvalidJwtException("Cannot find any member with this token");
        }
    }

    // TODO : private 로 수정할 필요가 있음.
    private boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKeySpec).build()
                    .parseClaimsJws(token);
            return true;
        } catch(Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return (getMemberId(token).equals(userDetails.getUsername()) && isValidToken(token));
    }
}

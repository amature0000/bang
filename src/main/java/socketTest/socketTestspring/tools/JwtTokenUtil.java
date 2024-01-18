package socketTest.socketTestspring.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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

    public String createToken(String memberId){
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .claim(MEMBER, memberId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRE_TIME))
                .signWith(secretKeySpec)
                .compact();
    }

    public String getMemberId(String token) throws ExpiredJwtException {
        try{
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(secretKeySpec).build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get(MEMBER).toString();
        } catch(ExpiredJwtException e) {
            log.warn("expired Jwt token. Need to refresh");
            throw e;
        } catch (Exception e) {
            log.error("Cannot find any member with this token");
            return null;
        }
    }

    /**
     * @deprecated Currently not in use. {@link JwtTokenUtil#getMemberId(String)} contains this method logically.
     */
    private boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKeySpec).build()
                    .parseClaimsJws(token);
            return true;
        } catch(ExpiredJwtException e) {
            log.warn("expired Jwt token. Need to refresh");
            return false;
        } catch(Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return (getMemberId(token).equals(userDetails.getUsername()));
    }
}

package socketTest.socketTestspring.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {
    private static final long SECOND = 1000;
    private static final long HOUR = 60 * 60;
    private static final long DAY = 24;

    //token expiration time
    public static final long EXPIRE_TIME = HOUR * SECOND;
    public static final long REFRESH_EXPIRE_TIME = DAY * HOUR * SECOND;
    private static final long REFRESH_TIME = DAY * HOUR * SECOND / 4;

    //token claim key
    private static final String MEMBER = "memberId";

    //token secret key
    @Value("${jwt.token.secret}")
    private String secretKey;

    // HS512 알고리즘을 사용하여 secretKey 이용해 서명
    public SecretKeySpec makeSecretKey() {
        return new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }

    public String createToken(String memberId){
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .claim(MEMBER, memberId)
                .setIssuedAt(new Date(now))//현재 시간
                .setExpiration(new Date(now + EXPIRE_TIME))//현재 시간 + 유효 시간 = 만료 시간
                .signWith(makeSecretKey())
                .compact();
    }

    public String getMemberId(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(makeSecretKey()).build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(MEMBER).toString();
    }

    // TODO : private 로 수정할 필요가 있음.
    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(makeSecretKey()).build()
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

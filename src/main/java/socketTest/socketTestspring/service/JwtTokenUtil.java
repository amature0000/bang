package socketTest.socketTestspring.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtil {
    public static String createToken(String memberId, String key, long expireTimeMs){
        Claims claims = Jwts.claims();
        claims.put("memberId",memberId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))//현재 시간
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))//현재 시간 + 종료 시간 = 만료 시간
                .signWith(new SecretKeySpec(key.getBytes(),SignatureAlgorithm.HS512.getJcaName()))// HS512 알고리즘을 사용하여 secretKey를 이용해 서명
                .compact();
    }
}

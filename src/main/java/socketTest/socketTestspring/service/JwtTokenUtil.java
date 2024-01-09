package socketTest.socketTestspring.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtTokenUtil {
    public static String createToken(String memberId, String key, long expireTimeMs){
        Claims claims = Jwts.claims();
        claims.put("memberId",memberId);
        Key encodedKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); //해싱 알고리즘으로 입력받은 키를 암호화

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))//현재시간
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))//현재시간 + 종료시간 = 만료 시간
                .signWith(encodedKey)
                .compact();
    }
}

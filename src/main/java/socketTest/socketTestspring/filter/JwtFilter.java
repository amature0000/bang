package socketTest.socketTestspring.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import socketTest.socketTestspring.service.JwtMemberDetailsService;
import socketTest.socketTestspring.tools.JwtTokenUtil;

import java.io.IOException;

//Transactional 추가 시 오류 발생. 참고 : https://stackoverflow.com/questions/60267832/nullpointerexception-spring-security-filter-failed
@Slf4j
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtMemberDetailsService jwtMemberDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractJwtFromRequest(request);
        try {
            if (token == null) {int breaker = 1 / 0;}
            String memberId = jwtTokenUtil.getMemberId(token);
            log.info("get memberId({}) from token", memberId);
            if (memberId == null) {int breaker = 1 / 0;}
            UserDetails memberDetails = jwtMemberDetailsService.loadUserByUsername(memberId);
            log.info("created UserDetails from memberId");
            if (memberDetails == null) {int breaker = 1 / 0;}
            if (!jwtTokenUtil.validateToken(token, memberDetails)) {int breaker = 1 / 0;}
            log.info("your user info stored at Security Context");
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        } catch (ExpiredJwtException e) {
            log.error("jwt expired : {}", token);
        } catch (UsernameNotFoundException e) {
            log.error("Cannot find any users with this token {}", token);
        } catch (ArithmeticException e) {
            //의도된 예외(1/0) 발생.
            log.warn("error occurred while validating(or getting) your Jwt token : {}", token);
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

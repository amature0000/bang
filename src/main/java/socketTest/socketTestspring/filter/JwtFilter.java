package socketTest.socketTestspring.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import socketTest.socketTestspring.exception.MyException;
import socketTest.socketTestspring.exception.myExceptions.GameRuleErrorCode;
import socketTest.socketTestspring.exception.myExceptions.classes.InvalidJwtException;
import socketTest.socketTestspring.exception.myExceptions.classes.JwtNotFoundException;
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
            if (token == null) throw new JwtNotFoundException("token does not exist in header");
            String memberId = jwtTokenUtil.getMemberId(token);
            log.info("get memberId : {}", memberId);

            if (memberId == null) throw new InvalidJwtException("MemberId is null");
            UserDetails memberDetails = jwtMemberDetailsService.loadUserByUsername(memberId);
            log.info("created UserDetails : {}", memberDetails);

            if (!jwtTokenUtil.validateToken(token, memberDetails)) throw new InvalidJwtException("Your token is invalid");

            log.info("your user info stored at Security Context");
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        } catch (JwtNotFoundException e) {
            log.warn("You are not authorized : {}", e.getMessage());
        } catch (InvalidJwtException e) {
            log.error("Invalid token : {}", e.getMessage());
            throw new MyException(GameRuleErrorCode.BAD_TOKEN_ACCESS, "Invalid token");
        } catch (UsernameNotFoundException e) {
            log.error("Cannot find any users with this token {}", token);
            throw new MyException(GameRuleErrorCode.BAD_USER_ACCESS, "");
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION); //Authorization
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

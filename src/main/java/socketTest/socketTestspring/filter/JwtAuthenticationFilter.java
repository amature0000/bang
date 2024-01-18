package socketTest.socketTestspring.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.annotation.Nullable;
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
import socketTest.socketTestspring.exception.MyResponse;
import socketTest.socketTestspring.exception.myExceptions.ErrorCode;
import socketTest.socketTestspring.exception.myExceptions.JwtErrorCode;
import socketTest.socketTestspring.service.JwtMemberDetailsService;
import socketTest.socketTestspring.tools.JwtTokenUtil;

import java.io.IOException;

// Transactional 추가 시 오류 발생. 참고 : https://stackoverflow.com/questions/60267832/nullpointerexception-spring-security-filter-failed
@Slf4j
@Component
@NonNullApi
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtMemberDetailsService jwtMemberDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (servletPath.equals("/api/users/login") || servletPath.equals("/api/users/join")) { //early return logic
            filterChain.doFilter(request, response);
            return;
        }
        String token = extractJwtFromRequest(request);
        if (token == null) {
            log.error("Token does not exists.");
            filterExceptionHandler(response, JwtErrorCode.TOKEN_NOT_EXIST);
            return;
        }
        try {
            String memberId = jwtTokenUtil.getMemberId(token); // Possible exception: ExpiredJwtException may occur.
            log.info("get memberId : {}", memberId); //memberId can be null

            UserDetails memberDetails = jwtMemberDetailsService.loadUserByUsername(memberId); // Possible exception: UsernameNotFoundException may occur.
            log.info("created UserDetails : {}", memberDetails); //memberDetails must not be null

            if (!jwtTokenUtil.validateToken(token, memberDetails)) {
                log.error("올바른 토큰이 아닙니다 : {}", token);
                filterExceptionHandler(response, JwtErrorCode.BAD_TOKEN);
                return;
            }

            log.info("유저 정보가 Security Context 에 저장됩니다.");
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            filterChain.doFilter(request, response);
        }
        catch(ExpiredJwtException e) {
            log.error("Expired Jwt token : {}", e.getMessage());
            filterExceptionHandler(response, JwtErrorCode.TOKEN_EXPIRED);
        }
        catch(UsernameNotFoundException e) {
            log.error("멤버 아이디가 없습니다 : {}", e.getMessage());
            filterExceptionHandler(response, JwtErrorCode.BAD_TOKEN);
        }
    }

    public void filterExceptionHandler(HttpServletResponse response, ErrorCode error) {
        response.setStatus(error.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(MyResponse.error(error.getMessage()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    @Nullable
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION); //Authorization
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

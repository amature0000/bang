package socketTest.socketTestspring.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import socketTest.socketTestspring.exception.MyResponse;
import socketTest.socketTestspring.exception.myExceptions.ErrorCode;
import socketTest.socketTestspring.service.JwtMemberDetailsService;
import socketTest.socketTestspring.tools.JwtTokenUtil;

import java.io.IOException;

import static socketTest.socketTestspring.exception.myExceptions.JwtErrorCode.*;

// Transactional 추가 시 오류 발생. 참고 : https://stackoverflow.com/questions/60267832/nullpointerexception-spring-security-filter-failed
@Slf4j
@Component
@NonNullApi
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtMemberDetailsService jwtMemberDetailsService;
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (servletPath.equals("/api/users/login") || servletPath.equals("/api/users/join")|| servletPath.equals("/ws")) { //early return logic
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtTokenUtil.getHeaderToken(request, "Access");
        String refreshToken = jwtTokenUtil.getHeaderToken(request, "Refresh");
        if (accessToken== null || refreshToken == null) {
            filterExceptionHandler(response, TOKEN_NOT_EXIST);
            return;
        }
        try {
            String memberId = jwtTokenUtil.getMemberId(accessToken, "Access"); // Possible exception: ExpiredJwtException may occur.
            UserDetails memberDetails = jwtMemberDetailsService.loadUserByUsername(memberId); // Possible exception: UsernameNotFoundException may occur.
            setAuthentication(memberDetails,request);
            filterChain.doFilter(request, response);
        }
        catch(UsernameNotFoundException e) {
            filterExceptionHandler(response, BAD_TOKEN);
        }
        catch(ExpiredJwtException e) {
            if(jwtTokenUtil.refreshTokenValidation(refreshToken)){
                String memberId = jwtTokenUtil.getMemberId(refreshToken,"Refresh");
                UserDetails memberDetails = jwtMemberDetailsService.loadUserByUsername(memberId); // Possible exception: UsernameNotFoundException may occur.
                String newAccessToken = jwtTokenUtil.createAccessToken(memberId);
                jwtTokenUtil.setHeaderAccessToken(response,newAccessToken);
                setAuthentication( memberDetails, request);
                return;
            }
            filterExceptionHandler(response, BAD_TOKEN);
        }
    }
    public void setAuthentication( UserDetails memberDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
    public void filterExceptionHandler(HttpServletResponse response, ErrorCode error) {
        log.error("{} : {}", error.getHttpStatus(), error.getMessage());
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
}

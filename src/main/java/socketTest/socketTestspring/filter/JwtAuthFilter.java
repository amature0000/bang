package socketTest.socketTestspring.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNullApi;
import io.micrometer.common.lang.Nullable;
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
import socketTest.socketTestspring.tools.TokenType;

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
        String accessToken = getTokenFromHeader(request, TokenType.ACCESS);
        String refreshToken = getTokenFromHeader(request, TokenType.REFRESH);
        if (accessToken== null || refreshToken == null) {
            filterExceptionHandler(response, TOKEN_NOT_EXIST);
            return;
        }
        try {
            String accessTokenMemberId = jwtTokenUtil.getMemberId(accessToken, TokenType.ACCESS); // Possible exception: ExpiredJwtException may occur.
            UserDetails memberDetails = jwtMemberDetailsService.loadUserByUsername(accessTokenMemberId); // Possible exception: UsernameNotFoundException may occur.
            setAuthentication(memberDetails,request);
            filterChain.doFilter(request, response);
        }
        catch(UsernameNotFoundException e) {
            filterExceptionHandler(response, BAD_TOKEN);
        }
        catch(ExpiredJwtException e) {
            // Refresh Token을 검증하고, 새 Access Token을 발급한다.
            try{
                String refreshTokenMemberId = jwtTokenUtil.getMemberId(refreshToken, TokenType.REFRESH); // Possible exception: ExpiredJwtException may occur.
                if(!jwtTokenUtil.refreshTokenValidation(refreshTokenMemberId, refreshToken)) { // UsernameNotFoundException may occur.
                    filterExceptionHandler(response, BAD_TOKEN);
                    return;
                }
                // TODO : Refresh token and access token must have the same id
                String newAccessToken = jwtTokenUtil.createToken(refreshTokenMemberId, TokenType.ACCESS);
                setHeader(response,newAccessToken, TokenType.ACCESS);

                // refresh token 으로 사용자 인가를 내주고 있었다. new access token 을 사용하는 것과 논리적인 차이는 없지만 일관성을 위해 수정하였음.
                String newAccessTokenMemberId = jwtTokenUtil.getMemberId(newAccessToken, TokenType.ACCESS);
                UserDetails memberDetails = jwtMemberDetailsService.loadUserByUsername(newAccessTokenMemberId); // Possible exception: UsernameNotFoundException may occur.
                setAuthentication(memberDetails, request);
                filterChain.doFilter(request, response);
            }
            catch (UsernameNotFoundException ex) {
                filterExceptionHandler(response, BAD_TOKEN);
            }
            catch (ExpiredJwtException ex) {
                filterExceptionHandler(response, REFRESH_TOKEN_EXPIRED);
            }
        }
    }
    @Nullable
    private String getTokenFromHeader(HttpServletRequest request, TokenType type){
        return request.getHeader(type.getHeader());
    }

    private void setHeader(HttpServletResponse response, String token, TokenType type) {
        response.setHeader(type.getHeader(), token);
    }

    private void setAuthentication(UserDetails memberDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
    private void filterExceptionHandler(HttpServletResponse response, ErrorCode error) {
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

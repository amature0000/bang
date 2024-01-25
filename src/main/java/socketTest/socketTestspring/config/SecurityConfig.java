package socketTest.socketTestspring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import socketTest.socketTestspring.filter.JwtAuthFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    //Http Security 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) //REST API 에서 CSRF 보안이 필요 없기 때문에 비 활성화
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                .formLogin(AbstractHttpConfigurer::disable) //기본 로그인 페이지 disable()
                .httpBasic(AbstractHttpConfigurer::disable)// 기본 UI 사용, 사용 하지 않을 시 disable()
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(request -> request
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("api/users/login","api/users/join").permitAll()
                        .requestMatchers(HttpMethod.POST,"api/**").authenticated() //api 경로의 post 방식은 인가 필요
                        .requestMatchers("ws/**").permitAll() //stomp handshake // TODO : 보안 기능 해제됨
                        .requestMatchers("test/**").permitAll() // TODO : 보안 기능 해제됨
                )

                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //STATELESS 로 설정함 으로서 세션 사용 X ,JWT 토큰을 사용할 것이기 때문
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // authorization 이전에 jwtFilter 실행
                .build();
    }
    //Pwd 인코딩
    @Bean
    public BCryptPasswordEncoder encodedPwd(){
        return new BCryptPasswordEncoder();
    }
}

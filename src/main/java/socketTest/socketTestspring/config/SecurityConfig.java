package socketTest.socketTestspring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import socketTest.socketTestspring.filter.JwtAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder(); //패스워드 인코딩
    }

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
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
                        .requestMatchers("api/**").authenticated()
                        .requestMatchers("ws").authenticated() //stomp handshake
                ) //TODO : authenticated() denied 시 아무 일도 일어나지 않음. 클라이언트에게 알림을 보내야 할 듯?

                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //STATELESS 로 설정함 으로서 세션 사용 X ,JWT 토큰을 사용할 것이기 때문
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // authorization 이전에 jwtFilter 실행
                .build();
    }
}

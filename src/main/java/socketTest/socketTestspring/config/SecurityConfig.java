package socketTest.socketTestspring.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    //Http Security 설정
    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable) //기본 로그인 페이지 disable()
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request  // 요청에 대한 사용 권한을 체크
                        .requestMatchers("/api/**").permitAll() //requestMatchers 파라미터로 설정한 리소스 접근을 인증절차 없이 허용
                        //.requestMatchers("/api/v1/users/join").permitAll()
                        //.requestMatchers("/api/v1/users/login").permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable) //REST API에서 CSRF 보안이 필요없기 때문에 비활성화
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ) //STATELESS 로 설정함 으로서 세션 사용 X ,JWT 토큰을 사용할 것이기 때문
                .httpBasic(AbstractHttpConfigurer::disable)// 기본 UI 사용, 사용하지 않을 시 disable()
                .build();
    }
}

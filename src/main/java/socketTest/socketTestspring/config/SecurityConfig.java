package socketTest.socketTestspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {
    //Http Security 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 UI 사용, 사용하지 않을 시 disable()
                .csrf(AbstractHttpConfigurer::disable) //REST API에서 CSRF 보안이 필요없기 때문에 비활성화
                .cors(Customizer.withDefaults())
                .authorizeRequests((authz) -> authz  // 요청에 대한 사용 권한을 체크
                        .requestMatchers("/api/**").permitAll() //requestMatchers 파라미터로 설정한 리소스 접근을 인증절차 없이 허용
                        .requestMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ) //STATELESS로 설정함으로서 인증 정보를 서버에 담아두지 않음,JWT 토큰을 사용할 것이기 때문
                .build();
    }
}

package socketTest.socketTestspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class EncryptorConfig {
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder(); //패스워드 인코딩
        //BCryptPasswordEncoder 는 시큐리티 프레임워크에서 제공하는 클래스 중 하나, 비밀번호를 암호화 하는데 사용할 수 있는 메서드 있음
    }
}

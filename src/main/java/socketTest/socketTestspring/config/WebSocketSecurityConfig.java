package socketTest.socketTestspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

// 참고 : https://docs.spring.io/spring-security/reference/servlet/integrations/websocket.html
// TODO : 아직 작동 하는지 테스트 하지 않았음. 미구현 상태, 작동 확인 시 WebSocketConfig 파일과 병합
@Configuration
public class WebSocketSecurityConfig {
    @Bean
    public MessageMatcherDelegatingAuthorizationManager.Builder messagesBuilder() {
        return MessageMatcherDelegatingAuthorizationManager.builder();
    }

    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages
                .simpSubscribeDestMatchers("/sub/**").permitAll()
                .simpDestMatchers("/pub/**").permitAll()
                .anyMessage().denyAll();

        return messages.build();
    }
}
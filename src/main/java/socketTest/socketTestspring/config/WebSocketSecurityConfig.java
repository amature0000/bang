package socketTest.socketTestspring.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

// 참고 : https://docs.spring.io/spring-security/reference/servlet/integrations/websocket.html
// CSRF disable 을 위해서 deprecated 된 버전을 사용해야 함(최신버전 에서는 지원하지 않음)
// TODO : deny 시 http status 200이 뜨는 것인지 테스트 툴에서는 success 로 찍힌다. 현재 deprecated 버전을 사용 중이기 때문에 인터넷에서 예시 확인하고 수정하면 될 듯.
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
        message
                .nullDestMatcher().permitAll() //Jwt validation at WebSocketInterceptor
                .simpSubscribeDestMatchers("/sub/**").authenticated()
                .simpDestMatchers("/pub/**").authenticated()
                .anyMessage().denyAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
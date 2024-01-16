package socketTest.socketTestspring.config;
import io.micrometer.common.lang.NonNullApi;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import socketTest.socketTestspring.tools.JwtTokenUtil;

import java.util.Objects;

@Component
@NonNullApi
@AllArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        System.out.println(accessor);

        if(Objects.equals(accessor.getCommand(), StompCommand.CONNECT)) {
            handleConnect(accessor);
            return message;
        }

        if (Objects.equals(accessor.getCommand(), StompCommand.DISCONNECT)) {
            handleDisconnect(accessor);
            return message;
        }

        if (Objects.equals(accessor.getCommand(), StompCommand.SUBSCRIBE)) {
            handleSubscribe(accessor);
            return message;
        }

        if (Objects.equals(accessor.getCommand(), StompCommand.SEND)) {
            handleSend(accessor);
            return message;
        }

        throw new IllegalStateException("Invalid operation. An unexpected command occurred during WebSocket connection.");
    }
    //TODO : 만약 WebSocketSecurityConfig 로 인증된다면 필요없어짐.
    private void handleConnect(StompHeaderAccessor accessor) {

    }
    private void handleDisconnect(StompHeaderAccessor accessor) {
        // DISCONNECT 요청 처리 로직
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        // SUBSCRIBE 요청 처리 로직
        // TODO : 해당 방에 입장 가능한 상황인지 검사
    }

    private void handleSend(StompHeaderAccessor accessor) {
        // SEND 요청 처리 로직
        // TODO : 해당 방에 유저가 SUBSCRIBE 되어 있는지 검사
    }
}

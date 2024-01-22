package socketTest.socketTestspring.config;

import io.micrometer.common.lang.NonNullApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Objects;
// 참고 : https://github.com/U-Zo/baesinzer/blob/master/server/src/main/java/projectw/baesinzer/controller/MessageController.java#L29
@Slf4j
@Component
@NonNullApi
@AllArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        
        log.info("{}", accessor);
        if(accessor == null) {
            throw new IllegalStateException("No message found.");
        }

        StompCommand command = accessor.getCommand();
        if(Objects.equals(command, StompCommand.CONNECT)) {
            handleConnect(accessor);
            return message;
        }

        if (Objects.equals(command, StompCommand.DISCONNECT)) {
            handleDisconnect(accessor);
            return message;
        }

        if (Objects.equals(command, StompCommand.SUBSCRIBE)) {
            handleSubscribe(accessor);
            return message;
        }

        if (Objects.equals(command, StompCommand.SEND)) {
            handleSend(accessor);
            return message;
        }

        throw new IllegalStateException("Invalid operation. An unexpected command occurred during WebSocket connection.");
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        // CONNECT 요청 처리 로직
        // Room에 접속

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

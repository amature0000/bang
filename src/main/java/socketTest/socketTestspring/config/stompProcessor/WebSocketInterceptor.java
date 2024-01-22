package socketTest.socketTestspring.config.stompProcessor;

import io.micrometer.common.lang.NonNullApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import socketTest.socketTestspring.service.MessageService;

import java.util.Objects;

// 참고 : https://velog.io/@jkijki12/%EC%B1%84%ED%8C%85-STOMP-JWT
@Slf4j
@Component
@NonNullApi
@AllArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    final private MessageService messageService;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) throw new IllegalStateException("No message found.");
        log.info("accessor :\n{}", accessor);

        StompCommand command = accessor.getCommand();
        if (Objects.equals(command, StompCommand.CONNECT)) {
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

    // MessageDeliveryException 만을 발생시켜야 하며, 구분은 안의 String 으로 한다. (예 : SEND 에서 예외 발생 시 "SEND"를 담음)

    private void handleConnect(StompHeaderAccessor accessor) throws MessageDeliveryException {
        // CONNECT 요청 처리 로직
        // TODO : 방 join 요청 날리기 -> 실패 시 Exception
    }

    private void handleDisconnect(StompHeaderAccessor accessor) throws MessageDeliveryException {
        // DISCONNECT 요청 처리 로직
        // TODO : 방에서 탈퇴하는 요청 날리기
    }

    private void handleSubscribe(StompHeaderAccessor accessor) throws MessageDeliveryException {
        // SUBSCRIBE 요청 처리 로직
        // TODO : 해당 방에 입장 가능한 상황인지 검사
    }

    private void handleSend(StompHeaderAccessor accessor) throws MessageDeliveryException {
        // SEND 요청 처리 로직
        // TODO : send 요청에 대한 유효성 검사
    }
}

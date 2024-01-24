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
import socketTest.socketTestspring.service.MemberRoomService;

import java.util.Objects;

// 참고 : https://velog.io/@jkijki12/%EC%B1%84%ED%8C%85-STOMP-JWT
@Slf4j
@Component
@NonNullApi
@AllArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    final private MemberRoomService messageService;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) throw new IllegalStateException("No message found.");
        log.info("accessor :\n{}", accessor);

        StompCommand command = accessor.getCommand();
        /*
        if (Objects.equals(command, StompCommand.CONNECT)) {
            handleConnect(accessor);
        }
        */

        if (Objects.equals(command, StompCommand.DISCONNECT)) {
            handleDisconnect(accessor);
        }
        if (Objects.equals(command, StompCommand.SUBSCRIBE)) {
            handleSubscribe(accessor);
        }

        if (Objects.equals(command, StompCommand.SEND)) {
            handleSend(accessor);
        }
        return message;
    }

    // MessageDeliveryException 만을 발생시켜야 하며, 구분은 안의 String 으로 한다.
    /*
    private void handleConnect(StompHeaderAccessor accessor) throws MessageDeliveryException {
        // CONNECT 요청 처리 로직
    }
    */
    private void handleDisconnect(StompHeaderAccessor accessor) throws MessageDeliveryException {
        // DISCONNECT 요청 처리 로직
        // TODO : 접속중인 방들로부터 탈퇴 처리를 해야 할까?
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

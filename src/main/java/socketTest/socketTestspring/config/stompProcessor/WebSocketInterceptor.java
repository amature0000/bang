package socketTest.socketTestspring.config.stompProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import socketTest.socketTestspring.service.RoomService;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static socketTest.socketTestspring.config.stompProcessor.StompErrorCode.*;

// 참고 : https://velog.io/@jkijki12/%EC%B1%84%ED%8C%85-STOMP-JWT
@Slf4j
@Component
@NonNullApi
@AllArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    final private RoomService roomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) throw new MessageDeliveryException(BAD_HEADER.toString());
        log.info("{}", accessor);

        StompCommand command = accessor.getCommand();
        /*
        if (Objects.equals(command, StompCommand.CONNECT)) handleConnect(accessor);

        if (Objects.equals(command, StompCommand.DISCONNECT)) handleDisconnect(accessor);
        */
        if (Objects.equals(command, StompCommand.SUBSCRIBE)) handleSubscribe(accessor);

        if (Objects.equals(command, StompCommand.UNSUBSCRIBE)) handleUnSubscribe(accessor);

        if (Objects.equals(command, StompCommand.SEND)) handleSend(message, accessor);

        return message;
    }

    // MessageDeliveryException 만을 발생시켜야 하며, 구분은 안의 String 으로 한다.

    private void handleConnect(StompHeaderAccessor accessor) throws MessageDeliveryException {
        // TODO : CONNECT 요청 처리 로직
    }

    private void handleDisconnect(StompHeaderAccessor accessor) throws MessageDeliveryException {
        // TODO : DISCONNECT 요청 처리 로직
    }

    private void handleSubscribe(StompHeaderAccessor accessor) throws MessageDeliveryException {
        // 경로 유효성 검사
        String destination = accessor.getDestination();
        if (destination == null) throw new MessageDeliveryException(BAD_HEADER.toString());
        if (!destination.startsWith("/sub/channel/")) throw new MessageDeliveryException(ILLEGAL_PATH.toString());
        String roomId = destination.substring(13);
        // 요청 수행
        log.info("Joining... room id : {}", roomId);
        if (!roomService.joinRoom(roomId)) throw new MessageDeliveryException(ACTION_FAILED.toString());
    }

    private void handleUnSubscribe(StompHeaderAccessor accessor) throws MessageDeliveryException {
        // 경로 유효성 검사
        String destination = accessor.getFirstNativeHeader("id"); // SUBSCRIBE 메세지로부터 할당된 id
        if (destination == null) throw new MessageDeliveryException(BAD_HEADER.toString());
        if (!destination.startsWith("/sub/channel/")) throw new MessageDeliveryException(ILLEGAL_PATH.toString());
        String roomId = destination.substring(13);
        // 요청 수행
        log.info("Exiting... room id : {}", roomId);
        if (!roomService.exitRoom(roomId)) throw new MessageDeliveryException(ACTION_FAILED.toString());
    }

    // destination으로부터 메세지 전송 목적지를 추출할 수 없다. (/pub 경로로 설정되어 있기 때문)
    // 메세지로부터 channelId 데이터를 추출하여 검증한다.
    private void handleSend(Message<?> message, StompHeaderAccessor accessor) throws MessageDeliveryException {
        // 메세지의 목적지 추출
        Object payload = message.getPayload();
        if (!(payload instanceof byte[])) throw new MessageDeliveryException(BAD_HEADER.toString());
        String jsonPayload = new String((byte[]) payload, StandardCharsets.UTF_8);
        String roomId;
        try {
            roomId = new ObjectMapper().readTree(jsonPayload).get("channelId").asText();
        } catch (Exception e) {
            throw new MessageDeliveryException(BAD_HEADER.toString());
        }
        // 발행 경로 유효성 검사
        String destination = accessor.getDestination();
        if (destination == null) throw new MessageDeliveryException(BAD_HEADER.toString());

        boolean isOnboard = roomService.onboardCheck(roomId);
        boolean temp = destination.startsWith("/pub/game");
        if (!(destination.startsWith("/pub/chat") || temp) || (temp ^ isOnboard))
            throw new MessageDeliveryException(ILLEGAL_PATH.toString());
        // 요청 유효성 검사
        log.info("extracted channelId : {}", roomId);
        if (!roomService.isJoined(roomId)) throw new MessageDeliveryException(ACTION_FAILED.toString());
    }
}

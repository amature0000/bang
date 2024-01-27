package socketTest.socketTestspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import socketTest.socketTestspring.dto.MyMessage;


@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * /sub/channel/{channelId}     - 구독        <br>
     * /pub/hello                   - 발행
     * @param message Message dto
     */
    @MessageMapping("send")
    public void sendMessage(MyMessage message) {
        // TODO : message type 에 따른 적절한 검증 로직
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.channelId(), message);
    }
}

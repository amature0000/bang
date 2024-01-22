package socketTest.socketTestspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import socketTest.socketTestspring.dto.Message;


@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * /sub/channel/{channelId}     - 구독        <br>
     * /pub/hello                   - 발행
     * @param message Message dto
     */
    @MessageMapping("hello")
    public void sendMessage(Message message) {
        // TODO : Service 단에서 메세지 처리 후 발송
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), message);
    }
    @MessageMapping("error")
    public void errorMessage(Message message) {
        simpMessageSendingOperations.convertAndSend("/topic/error", message);
    }
}

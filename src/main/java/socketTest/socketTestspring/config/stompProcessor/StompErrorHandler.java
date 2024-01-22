package socketTest.socketTestspring.config.stompProcessor;

import lombok.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

import static socketTest.socketTestspring.exception.myExceptions.ServerConnectionErrorCode.BAD_STOMP_ACCESS;

@Component
public class StompErrorHandler extends StompSubProtocolErrorHandler {
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]>clientMessage,@NonNull Throwable ex) {
        String exceptionMessage = ex.getMessage();
        if(exceptionMessage.equals("SEND")) {
            return handleSendException(ex);
        }
        if(exceptionMessage.equals("SUBSCRIBE")) {
            return handleSubScribeException(ex);
        }
        return super.handleClientMessageProcessingError(clientMessage, ex);
    }
    // TODO : StompCommand 타입을 뭘로 정할지 고민 중.
    private Message<byte[]> handleSendException(Throwable ex) {
        return prepareErrorMessage(StompCommand.MESSAGE, ex.getMessage(), String.valueOf(BAD_STOMP_ACCESS));
    }

    private Message<byte[]> handleSubScribeException(Throwable ex) {
        return prepareErrorMessage(StompCommand.ERROR, ex.getMessage(), String.valueOf(BAD_STOMP_ACCESS));
    }

    private Message<byte[]> prepareErrorMessage(StompCommand stompCommand, String errorMessage, String errorCode) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(stompCommand);
        accessor.setMessage(errorCode);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}

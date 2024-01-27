package socketTest.socketTestspring.config.stompProcessor;

import lombok.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

import static socketTest.socketTestspring.config.stompProcessor.StompErrorCode.*;


@Component
public class StompErrorHandler extends StompSubProtocolErrorHandler {
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]>clientMessage,@NonNull Throwable ex) {
        String exceptionMessage = ex.getMessage();
        if(exceptionMessage.equals("Null")) {
            return errorHandleException("Cannot find headers", BAD_HEADER);
        }
        if(exceptionMessage.equals("Illegal path")) {
            return errorHandleException("This path is not allowed", BAD_PATH_ACCESS);
        }
        if(exceptionMessage.equals("Join failed")) {
            return messageHandleException("Cannot join the room", BAD_ROOM_ACCESS);
        }
        if(exceptionMessage.equals("Permission")) {
            return errorHandleException("You have no permission for this action", BAD_STOMP_ACCESS);
        }
        if(exceptionMessage.equals("Already")) {
            return messageHandleException("This action has already been attempted", BAD_ROOM_ACCESS);
        }
        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> errorHandleException(String message, StompErrorCode errorCode) {
        return prepareErrorMessage(StompCommand.ERROR, message, String.valueOf(errorCode));
    }

    private Message<byte[]> messageHandleException(String message, StompErrorCode errorCode) {
        return prepareErrorMessage(StompCommand.MESSAGE, message, String.valueOf(errorCode));
    }

    private Message<byte[]> prepareErrorMessage(StompCommand stompCommand, String errorMessage, String errorCode) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(stompCommand);
        accessor.setMessage(errorCode);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}

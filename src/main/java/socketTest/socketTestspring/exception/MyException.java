package socketTest.socketTestspring.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import socketTest.socketTestspring.exception.myExceptions.ErrorCode;

@Getter
@AllArgsConstructor
public class MyException extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;

    @Override
    public String toString() {
        if (message == null) return errorCode.getMessage();
        return String.format("%s : %s", errorCode.getMessage(), message);
    }
}


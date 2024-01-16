package socketTest.socketTestspring.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import socketTest.socketTestspring.exception.myExceptions.GameRuleErrorCode;

@Getter
@AllArgsConstructor
public class MyException extends RuntimeException{
    private final GameRuleErrorCode errorCode;
    private final String message;

    @Override
    public String toString() {
        if (message == null) return errorCode.getMessage();
        return String.format("%s : %s", errorCode.getMessage(), message);
    }
}


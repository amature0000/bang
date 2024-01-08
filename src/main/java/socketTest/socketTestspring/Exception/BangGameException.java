package socketTest.socketTestspring.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BangGameException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;

    @Override
    public String toString(){
        if (message == null) return errorCode.getMessage();
        return String.format("%s, %s", errorCode.getMessage(), message);
    }
}
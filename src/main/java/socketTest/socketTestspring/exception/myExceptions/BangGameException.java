package socketTest.socketTestspring.exception.myExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @deprecated 기존에 정의된 Exceptions 를 사용하는 게 더 나을 것 같음.
 */
@Getter
@AllArgsConstructor
public class BangGameException extends RuntimeException{
    private HttpStatusExceptionEnum errorCode;
    private String message;

    @Override
    public String toString(){
        if (message == null) return errorCode.getMessage();
        return String.format("%s, %s", errorCode.getMessage(), message);
    }
}


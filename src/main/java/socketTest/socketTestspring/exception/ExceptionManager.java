package socketTest.socketTestspring.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import socketTest.socketTestspring.exception.myExceptions.ErrorCode;

@Slf4j
@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(MyException.class)
    public ResponseEntity<?> bangGameExceptionHandler(MyException e){
        ErrorCode errorCode = e.getErrorCode();
        log.error(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(Response.error(errorCode.getMessage()));
    }
}
//Exception
//BangGameException 이 Throw 되면 여기서 Handling 하게 됨.
//ErrorCode enum 내부의 Error 들 중 어떤 Error 가 Throw 됐는지 가져 와서 body 에 띄워줌

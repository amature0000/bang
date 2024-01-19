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
                .body(MyResponse.error(errorCode.getMessage()));
    }
}

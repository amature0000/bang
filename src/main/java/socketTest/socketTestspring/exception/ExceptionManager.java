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
// 정의된 예외 발생 시 핸들러. 미사용 메소드로 주석 처리하였음.
    /*
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException e){
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(MyResponse.error(e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundExceptionHandler(EntityNotFoundException e){
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(MyResponse.error(e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsExceptionHandler(BadCredentialsException e){
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(MyResponse.error(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> illegalStateException(IllegalStateException e){
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(MyResponse.error(e.getMessage()));
    }
     */
}
//Exception
//BangGameException 이 Throw 되면 여기서 Handling 하게 됨.
//ErrorCode enum 내부의 Error 들 중 어떤 Error 가 Throw 됐는지 가져 와서 body 에 띄워줌

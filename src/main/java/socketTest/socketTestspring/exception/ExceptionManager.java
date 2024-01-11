package socketTest.socketTestspring.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import socketTest.socketTestspring.exception.myExceptions.ErrorCode;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(BangGameException.class)
    public ResponseEntity<?> bangGameExceptionHandler(BangGameException e){
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(MyResponse.error(errorCode.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(MyResponse.error(e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundExceptionHandler(EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(MyResponse.error(e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsExceptionHandler(BadCredentialsException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(MyResponse.error(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> IllegalStateException(BadCredentialsException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(MyResponse.error(e.getMessage()));
    }
}
//Exception
//BangGameException 이 Throw 되면 여기서 Handling 하게 됨.
//ErrorCode enum 내부의 Error 들 중 어떤 Error 가 Throw 됐는지 가져 와서 body 에 띄워줌

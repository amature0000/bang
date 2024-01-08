package socketTest.socketTestspring.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(BangGameException.class)
    public ResponseEntity<?> bangGameExceptionHandler(BangGameException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(Response.error(e.getErrorCode().getMessage()));
    }
}
//Exception
//BangGameException 이 Throw 되면 여기서 Handling 하게 됨.
//ErrorCode enum 내부의 Error 들 중 어떤 Error 가 Throw 됐는지 가져 와서 body 에 띄워줌

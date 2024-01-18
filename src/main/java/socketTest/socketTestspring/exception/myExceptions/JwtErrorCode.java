package socketTest.socketTestspring.exception.myExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum JwtErrorCode implements ErrorCode {
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),
    TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "토큰이 없습니다"),
    BAD_TOKEN(HttpStatus.BAD_REQUEST, "인증 실패");

    private final HttpStatus httpStatus;
    private final String message;
}
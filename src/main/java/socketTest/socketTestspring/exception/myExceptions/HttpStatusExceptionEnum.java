package socketTest.socketTestspring.exception.myExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 예외 문구 추가는 하는 중
 * @deprecated 기존에 정의된 Exceptions 를 사용하는 게 더 나을 것 같음.
 */
@AllArgsConstructor
@Getter
public enum HttpStatusExceptionEnum {
    //Member action Exceptions
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "user name is duplicated"),
    USER_NOT_FOUNDED(HttpStatus.NOT_FOUND, "not found error"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "bad Request"),
    //validation Exceptions
    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"Invalid or missing authentication token");

    private final HttpStatus httpStatus;
    private final String message;
}
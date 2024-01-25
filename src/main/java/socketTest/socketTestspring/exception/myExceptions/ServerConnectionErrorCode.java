package socketTest.socketTestspring.exception.myExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ServerConnectionErrorCode implements ErrorCode {
    BAD_USER_ACCESS(HttpStatus.BAD_REQUEST, "잘못된 유저의 접근입니다"),
    BAD_ROOM_ACCESS(HttpStatus.BAD_REQUEST, "잘못된 방의 접근입니다");

    private final HttpStatus httpStatus;
    private final String message;
}
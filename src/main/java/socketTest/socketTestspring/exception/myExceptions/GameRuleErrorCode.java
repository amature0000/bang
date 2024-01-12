package socketTest.socketTestspring.exception.myExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GameRuleErrorCode implements ErrorCode {
    //GAME RULES
    INVALID_CARD_SUBMISSION(HttpStatus.BAD_REQUEST, "손에 없는 카드를 제출했습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
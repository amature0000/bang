package socketTest.socketTestspring.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyResponse<T> {
    private String resultCode;
    private T result;

    public static MyResponse<Void> error(String resultCode){
        return new MyResponse<>(resultCode, null);
    }

    public static <T> MyResponse<T> success(T result){
        return new MyResponse<>("SUCCESS", result);
    }
}
//JSON 으로 ErrorCode 와 ErrorMessage 를 보기 위해 Response 생성
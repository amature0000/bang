package socketTest.socketTestspring.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class MemberLoginRequest {
    private String memberId;
    private String memberPassword;
}

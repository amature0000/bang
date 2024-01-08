package socketTest.socketTestspring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import socketTest.socketTestspring.domain.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class MemberJoinRequest {
    private String memberID;
    private String memberPassword;
    private String memberName;

    public Member toEntity(String memberPassword){
        return new Member(this.memberID, memberPassword, this.memberName);
    }
}

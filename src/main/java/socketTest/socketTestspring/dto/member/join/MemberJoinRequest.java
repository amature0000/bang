package socketTest.socketTestspring.dto.member.join;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import socketTest.socketTestspring.domain.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRequest {
    private String memberId;
    private String memberPassword;
    private String memberName;

    public Member toEntity(String memberPassword){
        return new Member(this.memberId, memberPassword, this.memberName);
    }
}

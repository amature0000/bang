package socketTest.socketTestspring.dto.join;

import lombok.AllArgsConstructor;
import lombok.Getter;
import socketTest.socketTestspring.domain.Member;

@Getter
@AllArgsConstructor
public class MemberJoinResponse { //사용자 응답 DTO
    private String memberId;
    private String memberPassword;

    public MemberJoinResponse(Member member){
        this.memberId = member.getMemberId();
        this.memberPassword = member.getMemberPassword();
    }

}

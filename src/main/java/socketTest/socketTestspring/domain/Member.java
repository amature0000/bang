package socketTest.socketTestspring.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import socketTest.socketTestspring.dto.join.MemberJoinRequest;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberId;
    @Column(nullable = false)
    private String memberPassword;

    @Column(nullable = false)
    private String memberName;

    public Member(String memberId, String memberPassword, String memberName){
        this.memberId = memberId;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
    }

    public Member(MemberJoinRequest memberJoinRequest){
        this.memberId = memberJoinRequest.getMemberId();
        this.memberPassword = memberJoinRequest.getMemberPassword();
        this.memberName = memberJoinRequest.getMemberName();

    }
}

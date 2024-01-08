package socketTest.socketTestspring.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import socketTest.socketTestspring.dto.MemberJoinRequest;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberID;
    @Column(nullable = false)
    private String memberPassword;

    @Column(nullable = false)
    private String memberName;

    public Member(String memberID, String memberPassword, String memberName){
        this.memberID = memberID;
        this.memberPassword = memberPassword;
        this.memberName = memberName;
    }

    public Member(MemberJoinRequest memberJoinRequest){
        this.memberID = memberJoinRequest.getMemberID();
        this.memberPassword = memberJoinRequest.getMemberPassword();
        this.memberName = memberJoinRequest.getMemberName();

    }

}

package socketTest.socketTestspring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.Member;
import socketTest.socketTestspring.dto.member.join.MemberJoinRequest;
import socketTest.socketTestspring.exception.MyException;
import socketTest.socketTestspring.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원가입성공() {
        //given
        MemberJoinRequest memberJoinRequest = new MemberJoinRequest("id", "pw", "name");
        //when
        Member joinedMember = memberService.join(memberJoinRequest);
        //then
        Member findMember = memberService.findById(joinedMember.getMemberId()).get();
        assertThat(memberJoinRequest.memberId()).isEqualTo(findMember.getMemberId());
    }

    @Test
    void 중복회원가입실패() {
        //given
        MemberJoinRequest memberJoinRequest1 = new MemberJoinRequest("duplicated id", "pw", "name");
        MemberJoinRequest memberJoinRequest2 = new MemberJoinRequest("duplicated id", "pw", "name");
        //when
        Member joinedMember = memberService.join(memberJoinRequest1);
        MyException e = assertThrows(MyException.class, () -> {
            memberService.join(memberJoinRequest2);
        });
        //then
        assertThat(e.getMessage()).isEqualTo("user Id is duplicated");
    }
}

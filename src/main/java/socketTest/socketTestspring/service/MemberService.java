package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.Exception.ErrorCode;
import socketTest.socketTestspring.Exception.BangGameException;
import socketTest.socketTestspring.domain.Member;
import socketTest.socketTestspring.repository.MemberRepository;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member join(Member member){
        memberRepository.findByMemberID(member.getMemberID())
                .ifPresent(member1 -> {
                    throw new BangGameException(ErrorCode.DUPLILCATED_USER_ID, String.format("UserId : %s",member1.getMemberID()));
                }); //같은 ID 가진 회원 존재시 HospitalReviewAppExceptio 에러 발생
        memberRepository.save(member);

        return member;
    }
}

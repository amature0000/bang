package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.Member;
import socketTest.socketTestspring.dto.TokenDto;
import socketTest.socketTestspring.dto.member.join.MemberJoinRequest;
import socketTest.socketTestspring.dto.member.login.MemberLoginRequest;
import socketTest.socketTestspring.exception.MyException;
import socketTest.socketTestspring.repository.MemberRepository;
import socketTest.socketTestspring.tools.JwtTokenUtil;

import static socketTest.socketTestspring.exception.myExceptions.ServerConnectionErrorCode.BAD_ROOM_ACCESS;
import static socketTest.socketTestspring.exception.myExceptions.ServerConnectionErrorCode.BAD_USER_ACCESS;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;

    public Member findById(String memberId) {
        return memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new MyException(BAD_ROOM_ACCESS, "Cannot find any member")
        );
    }

    @Transactional
    public Member join(MemberJoinRequest memberJoinRequest) {
        memberRepository.findByMemberId(memberJoinRequest.memberId())
                .ifPresent(member1 -> {
                    throw new MyException(BAD_USER_ACCESS, "user Id is duplicated");
                });
        String encodedPwd = encoder.encode(memberJoinRequest.memberPassword());
        Member member = new Member(memberJoinRequest.memberId(), encodedPwd, memberJoinRequest.memberName());
        memberRepository.save(member);

        return member;
    }

    public TokenDto login(MemberLoginRequest memberLoginRequest) {
        String memberId = memberLoginRequest.memberId();
        String memberPassword = memberLoginRequest.memberPassword();

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() ->  new MyException(BAD_USER_ACCESS, "wrong user Id or Password"));
        if(!encoder.matches(memberPassword, member.getMemberPassword())){
            throw new MyException(BAD_USER_ACCESS, "wrong user Id or Password");
        }
        return jwtTokenUtil.createAllToken(memberId);
    }
}
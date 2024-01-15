package socketTest.socketTestspring.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.Member;
import socketTest.socketTestspring.exception.MyException;
import socketTest.socketTestspring.exception.myExceptions.GameRuleErrorCode;
import socketTest.socketTestspring.repository.MemberRepository;
import socketTest.socketTestspring.tools.JwtTokenUtil;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public Member join(Member member){
        memberRepository.findByMemberId(member.getMemberId())
                .ifPresent(member1 -> {
                    throw new MyException(GameRuleErrorCode.BAD_USER_ACCESS, "user Id is duplicated");
                });
        memberRepository.save(member);

        return member;
    }

    public String login(String memberId, String memberPassword){
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() ->  new MyException(GameRuleErrorCode.BAD_USER_ACCESS, "wrong user Id or Password"));
        if(!encoder.matches(memberPassword, member.getMemberPassword())){
            throw new MyException(GameRuleErrorCode.BAD_USER_ACCESS, "wrong user Id or Password");
        }

        return jwtTokenUtil.createToken(memberId);
    }

    //TODO : 방 입장, 퇴장 관련 로직 구현
}

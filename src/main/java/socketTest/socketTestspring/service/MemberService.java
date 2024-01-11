package socketTest.socketTestspring.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.Member;
import socketTest.socketTestspring.repository.MemberRepository;
import socketTest.socketTestspring.tools.JwtTokenUtil;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public Member join(Member member){
        memberRepository.findByMemberId(member.getMemberId())
                .ifPresent(member1 -> {
                    throw new DataIntegrityViolationException("user Id is duplicated");
                }); //같은 ID 가진 회원 BangGameException 에러 발생
        memberRepository.save(member);

        return member;
    }

    @Value("${jwt.token.secret}")
    private String secretKey;
    private static final long EXPIRE_TIME = 1000* 60 * 60; //토큰 유지 시간 1시간

    public String login(String memberId, String memberPassword){
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() ->  new EntityNotFoundException("wrong user Id or Password"));

        if(!encoder.matches(memberPassword, member.getMemberPassword())){
            throw new EntityNotFoundException("wrong user Id or Password");
        }
        return JwtTokenUtil.createToken(memberId,secretKey,EXPIRE_TIME);
    }
}

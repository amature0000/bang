package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.Exception.ErrorCode;
import socketTest.socketTestspring.Exception.BangGameException;
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
                    throw new BangGameException(ErrorCode.DUPLICATED_USER_ID, String.format("UserId : %s",member1.getMemberId()));
                }); //같은 ID 가진 회원 BangGameException 에러 발생
        memberRepository.save(member);

        return member;
    }

    @Value("${jwt.token.secret}")
    private String secretKey;
    private final long expiredTimeMs = 1000* 60 * 60; //토큰 유지 시간 1시간

    public String login(String memberId, String memberPassword){
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BangGameException(ErrorCode.USER_NOT_FOUNDED,String.format("%s는 가입된 적이 없습니다.", memberId)));

        if(!encoder.matches(memberPassword, member.getMemberPassword())){
            throw new BangGameException(ErrorCode.INVALID_PASSWORD,"Id 또는 Password가 잘못 되었습니다.");
        }
        return JwtTokenUtil.createToken(memberId,secretKey,expiredTimeMs);
    }

}

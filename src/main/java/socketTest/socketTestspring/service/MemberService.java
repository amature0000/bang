package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.Member;
import socketTest.socketTestspring.domain.RefreshToken;
import socketTest.socketTestspring.dto.TokenDto;
import socketTest.socketTestspring.dto.member.join.MemberJoinRequest;
import socketTest.socketTestspring.exception.MyException;
import socketTest.socketTestspring.repository.MemberRepository;
import socketTest.socketTestspring.repository.RefreshTokenRepository;
import socketTest.socketTestspring.tools.JwtTokenUtil;

import java.util.Optional;

import static socketTest.socketTestspring.exception.myExceptions.ServerConnectionErrorCode.BAD_USER_ACCESS;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;

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

    public TokenDto login(String memberId, String memberPassword) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() ->  new MyException(BAD_USER_ACCESS, "wrong user Id or Password"));
        if(!encoder.matches(memberPassword, member.getMemberPassword())){
            throw new MyException(BAD_USER_ACCESS, "wrong user Id or Password");
        }

        TokenDto tokenDto = jwtTokenUtil.createTokens(memberId);
        //refreshToken 있는지 확인
        // TODO : 어차피 토큰이 있든 없든 새 토큰을 할당하는데, if-else 로 구분해야 할 이유가 없음. 또한 Optional 객체를 활용하지 않아 발생 가능한 예외들이 무시됨.
        // TODO : Refresh Token 을 재할당 하는 로직은 MemberService 의 login 메소드가 아닌 다른 곳에서 구현해야 적절함. 유저 정보 관리 Service 에 구현해야 하는 내용이 아님.
        // TODO : save 명령만 하고 있는데, 기존의 Token 데이터는 계속 쌓이기만 하는 건지? (위의 내용과 더불어) 데이터 처리 및 발생 가능한 에러에 대한 핸들링이 미흡함.
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberId(memberId);
        if(refreshToken.isPresent()){
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.refreshToken()));
        } else{
            RefreshToken newToken = new RefreshToken(tokenDto.refreshToken(), memberId);
            refreshTokenRepository.save(newToken);
        }
        return tokenDto;
    }
}
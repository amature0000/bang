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
    public Member join(MemberJoinRequest memberJoinRequest){
        memberRepository.findByMemberId(memberJoinRequest.memberId())
                .ifPresent(member1 -> {
                    throw new MyException(BAD_USER_ACCESS, "user Id is duplicated");
                });
        String encodedPwd = encoder.encode(memberJoinRequest.memberPassword());
        Member member = new Member(memberJoinRequest.memberId(), encodedPwd, memberJoinRequest.memberName());
        memberRepository.save(member);

        return member;
    }

    public TokenDto login(String memberId, String memberPassword){
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() ->  new MyException(BAD_USER_ACCESS, "wrong user Id or Password"));
        if(!encoder.matches(memberPassword, member.getMemberPassword())){
            throw new MyException(BAD_USER_ACCESS, "wrong user Id or Password");
        }

        TokenDto tokenDto = jwtTokenUtil.createAllToken(memberId);
        //refreshToken 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberId(memberId);
        if(refreshToken.isPresent()){
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.refreshToken()));
        } else{
            RefreshToken newToken = new RefreshToken(tokenDto.refreshToken(), memberId);
            refreshTokenRepository.save(newToken);
        }
        return tokenDto;
    }


    //TODO : 방 입장, 퇴장 관련 로직 구현
}

package socketTest.socketTestspring.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import socketTest.socketTestspring.domain.Member;
import socketTest.socketTestspring.dto.TokenDto;
import socketTest.socketTestspring.dto.member.join.MemberJoinRequest;
import socketTest.socketTestspring.dto.member.join.MemberJoinResponse;
import socketTest.socketTestspring.dto.member.login.MemberLoginRequest;
import socketTest.socketTestspring.dto.member.login.MemberLoginResponse;
import socketTest.socketTestspring.exception.MyResponse;
import socketTest.socketTestspring.service.MemberService;
import socketTest.socketTestspring.service.RefreshTokenService;
import socketTest.socketTestspring.tools.TokenType;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    @PostMapping("/join")
    public MyResponse<MemberJoinResponse> join(@RequestBody MemberJoinRequest memberJoinRequest){
        Member newMember = memberService.join(memberJoinRequest);
        MemberJoinResponse memberJoinResponse = new MemberJoinResponse(newMember.getMemberId(),newMember.getMemberPassword());
        return MyResponse.success(memberJoinResponse);
    }

    @PostMapping("/login")
    public MyResponse<MemberLoginResponse> login(@RequestBody MemberLoginRequest memberLoginRequest, HttpServletResponse response){
        TokenDto tokenDto = memberService.login(memberLoginRequest);
        refreshTokenService.updateRefreshToken(memberLoginRequest.memberId(), tokenDto); // must not throw any exceptions.
        response.addHeader(TokenType.ACCESS.getHeader(), tokenDto.accessToken());
        response.addHeader(TokenType.REFRESH.getHeader(), tokenDto.refreshToken());
        return MyResponse.success(new MemberLoginResponse(tokenDto));
    }


}

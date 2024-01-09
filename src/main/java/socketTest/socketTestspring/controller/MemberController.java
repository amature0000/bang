package socketTest.socketTestspring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import socketTest.socketTestspring.Exception.Response;
import socketTest.socketTestspring.domain.Member;
import socketTest.socketTestspring.dto.join.MemberJoinRequest;
import socketTest.socketTestspring.dto.join.MemberJoinResponse;
import socketTest.socketTestspring.dto.login.MemberLoginRequest;
import socketTest.socketTestspring.dto.login.MemberLoginResponse;
import socketTest.socketTestspring.service.MemberService;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final BCryptPasswordEncoder encoder;

    @PostMapping("/join")
    public Response<MemberJoinResponse> join(@RequestBody MemberJoinRequest memberJoinRequest){
        Member join = memberService.join(memberJoinRequest.toEntity(encoder.encode(memberJoinRequest.getMemberPassword())));
        MemberJoinResponse memberJoinResponse = new MemberJoinResponse(join);
        return Response.success(memberJoinResponse);
    }

    @PostMapping("/login")
    public Response<MemberLoginResponse> login(@RequestBody MemberLoginRequest memberLoginRequest){
        String token = memberService.login(memberLoginRequest.getMemberId(),memberLoginRequest.getMemberPassword());
        return Response.success(new MemberLoginResponse(token));
    }
}

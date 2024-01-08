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
import socketTest.socketTestspring.dto.MemberJoinRequest;
import socketTest.socketTestspring.dto.MemberJoinResponse;
import socketTest.socketTestspring.service.MemberService;

@RestController
@RequestMapping("/api/v1/users")
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
}

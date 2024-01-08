package socketTest.socketTestspring.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import socketTest.socketTestspring.dto.LoginDto;
import socketTest.socketTestspring.dto.MemberDto;
import socketTest.socketTestspring.service.MemberService;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
@Controller
public class MemberController {
    @Autowired
    MemberService memberService;


    @PostMapping("/create")
    public ResponseEntity<MemberDto> creatMember(@RequestBody MemberDto memberDto){
        MemberDto memberDto1 = memberService.createNewMember(memberDto);
        log.info("회원 가입 성공, Member Id = " + memberDto1.getId());
        return ResponseEntity.ok(memberDto1);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto, HttpSession httpSession){
        Long memberId = memberService.login(loginDto); //login = MemberID
        if(memberId == null){
            return ResponseEntity.badRequest().build();
        }
        httpSession.setAttribute("login",memberId); //닉네임으로 수정
        log.info("로그인 성공 " + memberId);
        return ResponseEntity.ok(memberId);
    }

    @GetMapping("/info")
    public ResponseEntity<MemberDto> getMemberInfo(HttpSession httpSession){
        Long memberId = (Long)httpSession.getAttribute("login");
        MemberDto memberDto = memberService.findById(memberId);
        log.info("회원 정보 조회 성공 " + memberId);
        return ResponseEntity.ok(memberDto);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpSession httpSession){
        if(httpSession != null){
            httpSession.invalidate();
            log.info("로그 아웃 성공");
        }
        return ResponseEntity.ok().build();
    }

}
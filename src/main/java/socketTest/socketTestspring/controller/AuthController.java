package socketTest.socketTestspring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import socketTest.socketTestspring.tools.JwtTokenUtil;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth")
    public boolean auth(@RequestBody String jwtToken) {
        return jwtTokenUtil.isValidToken(jwtToken);
    }
}

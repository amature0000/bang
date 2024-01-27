package socketTest.socketTestspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
public class HtmlController {
    @GetMapping("/stomp-test")
    public String stompTest() {
        return "/stompTest/StompTest";
    }
}

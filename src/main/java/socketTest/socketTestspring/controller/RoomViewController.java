package socketTest.socketTestspring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import socketTest.socketTestspring.dto.ChatRoomDto;
import socketTest.socketTestspring.service.RoomService;
import socketTest.socketTestspring.tools.DtoConverter;

import java.util.List;

//방 생성 및 조회 관련 view resolver

@Controller
@RequestMapping("/room")
public class RoomViewController {
    private final RoomService service;
    public RoomViewController(RoomService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public String viewAllRooms(Model model) {
        List<ChatRoomDto> chatRooms = DtoConverter.convertToDto(service.findAllRoom());
        model.addAttribute("chatRooms", chatRooms);
        return "/room/list/chatRooms";
    }

    @GetMapping("/room")
    public String gameRoom() {
        return "/room/privateRoom";
    }

    @GetMapping
    public String viewChatHome() {
        return "/room/home";
    }
}

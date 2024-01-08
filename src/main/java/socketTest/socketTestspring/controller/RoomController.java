package socketTest.socketTestspring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import socketTest.socketTestspring.dto.ChatRoomDto;
import socketTest.socketTestspring.service.RoomService;
import socketTest.socketTestspring.tools.DtoConverter;

import java.util.List;

//방 생성 및 조회 관련 API 통신

@Slf4j
@RestController
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService service;
    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping("/new")
    public ChatRoomDto createRoom(@RequestParam String name){
        return DtoConverter.convertToDto(service.createRoom(name));
    }

    @GetMapping
    public List<ChatRoomDto> findAllRooms(){
        return DtoConverter.convertToDto(service.findAllRoom());
    }
}

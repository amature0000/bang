package socketTest.socketTestspring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import socketTest.socketTestspring.domain.Room;
import socketTest.socketTestspring.dto.room.create.RoomCreateRequest;
import socketTest.socketTestspring.dto.room.create.RoomCreateResponse;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteRequest;
import socketTest.socketTestspring.exception.MyResponse;
import socketTest.socketTestspring.service.RoomService;

//방 생성 및 조회 관련 API 통신

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/new")
    public MyResponse<RoomCreateResponse> createRoom(@RequestBody RoomCreateRequest roomCreateRequest){
        Room room = roomService.createRoom(roomCreateRequest.toEntity());
        RoomCreateResponse roomCreateResponse = new RoomCreateResponse(room);
        return MyResponse.success(roomCreateResponse);
    }

    @PostMapping("/delete")
    public String deleteRoom(@RequestBody RoomDeleteRequest roomDeleteRequest){
        //TODO : token 으로 사용자를 식별하여 room 의 admin 권한이 있는지 검사

        return roomService.deleteRoom(roomDeleteRequest.getRoomId());
    }
}

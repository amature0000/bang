package socketTest.socketTestspring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import socketTest.socketTestspring.dto.room.create.RoomCreateRequest;
import socketTest.socketTestspring.dto.room.create.RoomCreateResponse;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteRequest;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteResponse;
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
        RoomCreateResponse roomCreateResponse = roomService.createRoom(roomCreateRequest);
        return MyResponse.success(roomCreateResponse);
    }

    @PostMapping("/delete")
    public MyResponse<RoomDeleteResponse> deleteRoom(@RequestBody RoomDeleteRequest roomDeleteRequest){
        RoomDeleteResponse roomDeleteResponse = roomService.deleteRoom(roomDeleteRequest);
        return MyResponse.success(roomDeleteResponse);
    }
}

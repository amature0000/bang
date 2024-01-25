package socketTest.socketTestspring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import socketTest.socketTestspring.dto.room.RoomDto;
import socketTest.socketTestspring.dto.room.create.RoomCreateRequest;
import socketTest.socketTestspring.dto.room.create.RoomCreateResponse;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteRequest;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteResponse;
import socketTest.socketTestspring.exception.MyResponse;
import socketTest.socketTestspring.service.RoomService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/list")
    public List<RoomDto> listRoom(){
        return roomService.roomList();
    }

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

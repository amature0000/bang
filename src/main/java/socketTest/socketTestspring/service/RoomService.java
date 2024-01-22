package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import socketTest.socketTestspring.domain.MemberInfo;
import socketTest.socketTestspring.domain.Room;
import socketTest.socketTestspring.dto.room.RoomDto;
import socketTest.socketTestspring.dto.room.create.RoomCreateRequest;
import socketTest.socketTestspring.dto.room.create.RoomCreateResponse;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteRequest;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteResponse;
import socketTest.socketTestspring.dto.room.join.RoomJoinRequest;
import socketTest.socketTestspring.dto.room.join.RoomJoinResponse;
import socketTest.socketTestspring.exception.MyException;
import socketTest.socketTestspring.repository.MemoryRoomRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static socketTest.socketTestspring.exception.myExceptions.ServerConnectionErrorCode.BAD_ROOM_ACCESS;


@Service
@RequiredArgsConstructor
public class RoomService {
    private final MemoryRoomRepository roomRepository;

    public List<RoomDto> roomList() {
        List<Room> roomList = new ArrayList<>(roomRepository.findAll().values());
        List<RoomDto> roomDtoList = new ArrayList<>(roomList.stream()
                .map(RoomDto::fromRoom)
                .toList());
        Collections.reverse(roomDtoList);
        return roomDtoList;
    }

    public Room findOne(String roomId) {
        return roomRepository.findByRoomId(roomId).orElseThrow(() ->
                new MyException(BAD_ROOM_ACCESS, "No rooms found")
        );
    }

    public RoomCreateResponse createRoom(RoomCreateRequest roomCreateRequest) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        Room room = new Room(roomCreateRequest.roomName(), memberId);
        roomRepository.saveRoom(room);
        return new RoomCreateResponse(room.getRoomId(), room.getRoomName());
    }

    public RoomDeleteResponse deleteRoom(RoomDeleteRequest roomDeleteRequest) {
        roomRepository.deleteByRoomId(roomDeleteRequest.roomId()).orElseThrow(() ->
                new MyException(BAD_ROOM_ACCESS, "No rooms found")
        );
        return new RoomDeleteResponse("room deleted");
    }

    // TODO : roomJoinRequest 에서 memberId를 그대로 받아오고 있음. 보안을 위해 memberId 대신 현재 접속중인 사용자 id만 request 로 날릴 수 있도록 수정 필요.
    public RoomJoinResponse joinRoom(RoomJoinRequest roomJoinRequest) {
        Room joinRoom = findOne(roomJoinRequest.roomId()); // Possible exception: MyException may be thrown.
        MemberInfo memberInfo = new MemberInfo(roomJoinRequest.memberId());

        boolean result = roomRepository.joinRoom(joinRoom, memberInfo);
        if (!result) throw new MyException(BAD_ROOM_ACCESS, "Cannot join the room");
        return new RoomJoinResponse("joined"); // TODO : response 문에 추가할 만한 데이터가 있으면 추가하기.
    }
}
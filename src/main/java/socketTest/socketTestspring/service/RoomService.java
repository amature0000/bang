package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.Room;
import socketTest.socketTestspring.dto.room.RoomDto;
import socketTest.socketTestspring.dto.room.create.RoomCreateRequest;
import socketTest.socketTestspring.dto.room.create.RoomCreateResponse;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteRequest;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteResponse;
import socketTest.socketTestspring.exception.MyException;
import socketTest.socketTestspring.repository.MemoryRoomRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static socketTest.socketTestspring.exception.myExceptions.ServerConnectionErrorCode.BAD_ROOM_ACCESS;


@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final MemoryRoomRepository roomRepository;

    public List<RoomDto> roomList() {
        List<RoomDto> roomDtoList = roomRepository.findAll().values().stream()
                .map(RoomDto::fromRoom)
                .collect(Collectors.toList());

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
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!findOne(roomDeleteRequest.roomId()).getOwnerMemberId().equals(memberId)) // Possible exception: MyException may be thrown.
            throw new MyException(BAD_ROOM_ACCESS, "You are not the owner of the room");

        roomRepository.deleteByRoomId(roomDeleteRequest.roomId()).orElseThrow(() ->
                new MyException(BAD_ROOM_ACCESS, "No rooms found")
        );
        return new RoomDeleteResponse("room deleted");
    }
}
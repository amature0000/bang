package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.Room;
import socketTest.socketTestspring.dto.room.create.RoomCreateRequest;
import socketTest.socketTestspring.dto.room.create.RoomCreateResponse;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteRequest;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteResponse;
import socketTest.socketTestspring.exception.MyException;
import socketTest.socketTestspring.exception.myExceptions.ServerConnectionErrorCode;
import socketTest.socketTestspring.repository.RoomRepository;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;

    @Transactional
    public RoomCreateResponse createRoom(RoomCreateRequest roomCreateRequest) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        Room created = roomRepository.save(new Room(roomCreateRequest.roomName(), memberId));
        return new RoomCreateResponse(created.getRoomId(), created.getRoomName());
    }

    @Transactional
    public RoomDeleteResponse deleteRoom(RoomDeleteRequest roomDeleteRequest) {
        Room deleteRoom = roomRepository.findByRoomId(roomDeleteRequest.roomId())
                .orElseThrow(() -> new MyException(ServerConnectionErrorCode.BAD_ROOM_ACCESS, "Cannot find any room with this roomId"));

        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!memberId.equals(deleteRoom.getOwnerMemberId())) throw new MyException(ServerConnectionErrorCode.BAD_USER_ACCESS, "This user is not the room owner");

        roomRepository.delete(deleteRoom);
        return new RoomDeleteResponse("room deleted");
    }
}
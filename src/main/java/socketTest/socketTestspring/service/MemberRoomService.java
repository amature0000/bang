package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.MemberInfo;
import socketTest.socketTestspring.domain.Room;
import socketTest.socketTestspring.dto.room.join.RoomJoinRequest;
import socketTest.socketTestspring.dto.room.join.RoomJoinResponse;
import socketTest.socketTestspring.exception.MyException;
import socketTest.socketTestspring.repository.MemoryRoomRepository;

import static socketTest.socketTestspring.exception.myExceptions.ServerConnectionErrorCode.BAD_ROOM_ACCESS;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberRoomService {
    private final MemoryRoomRepository roomRepository;

    private Room findOne(String roomId) throws MyException {
        return roomRepository.findByRoomId(roomId).orElseThrow(() ->
                new MyException(BAD_ROOM_ACCESS, "No rooms found")
        );
    }
    public RoomJoinResponse joinRoom(RoomJoinRequest roomJoinRequest) {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        Room joinRoom = findOne(roomJoinRequest.roomId()); // Possible exception: MyException may be thrown.
        MemberInfo memberInfo = new MemberInfo(memberId);

        boolean result = roomRepository.joinRoom(joinRoom, memberInfo);
        if (!result) throw new MyException(BAD_ROOM_ACCESS, "Cannot join the room");
        return new RoomJoinResponse("joined");
    }
}

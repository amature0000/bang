package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.MemberInfo;
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

import static socketTest.socketTestspring.config.stompProcessor.StompErrorCode.BAD_MESSAGE;
import static socketTest.socketTestspring.exception.myExceptions.ServerConnectionErrorCode.BAD_ROOM_ACCESS;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final MemoryRoomRepository roomRepository;

    public Room findOne(String roomId) throws MyException {
        return roomRepository.findByRoomId(roomId).orElseThrow(() ->
                new MyException(BAD_ROOM_ACCESS, "No rooms found")
        );
    }
    private String getUserId() {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("user Id : {}", memberId);
        return memberId;
    }
    //===== http request에서 사용되는 메소드
    public List<RoomDto> roomList() {
        List<RoomDto> roomDtoList = roomRepository.findAll().values().stream()
                .map(RoomDto::fromRoom)
                .collect(Collectors.toList());

        Collections.reverse(roomDtoList);
        return roomDtoList;
    }

    public RoomCreateResponse createRoom(RoomCreateRequest roomCreateRequest) {
        String memberId = getUserId();
        Room room = new Room(roomCreateRequest.roomName(), memberId);
        roomRepository.saveRoom(room);
        return new RoomCreateResponse(room.getRoomId(), room.getRoomName());
    }

    public RoomDeleteResponse deleteRoom(RoomDeleteRequest roomDeleteRequest) {
        String memberId = getUserId();
        if(!findOne(roomDeleteRequest.roomId()).getOwnerMemberId().equals(memberId)) // Possible exception: MyException may be thrown.
            throw new MyException(BAD_ROOM_ACCESS, "You are not the owner of the room");

        roomRepository.deleteByRoomId(roomDeleteRequest.roomId()).orElseThrow(() ->
                new MyException(BAD_ROOM_ACCESS, "No rooms found")
        );
        return new RoomDeleteResponse("room deleted");
    }
    //==== 웹소켓 인터셉터에서 사용되는 메소드
    public boolean joinRoom(String roomId) throws MessageDeliveryException {
        String memberId = getUserId();
        Room joinRoom = roomRepository.findByRoomId(roomId).orElseThrow(() ->
                new MessageDeliveryException(BAD_MESSAGE.toString()));

        return roomRepository.joinRoom(joinRoom, new MemberInfo(memberId));
    }

    public boolean exitRoom(String roomId) throws MessageDeliveryException {
        String memberId = getUserId();
        Room exitRoom = roomRepository.findByRoomId(roomId).orElseThrow(() ->
                new MessageDeliveryException(BAD_MESSAGE.toString()));

        return roomRepository.quitRoom(exitRoom, new MemberInfo(memberId));
    }

    public boolean isJoined(String roomId) throws MessageDeliveryException {
        String memberId = getUserId();
        Room byRoomId = roomRepository.findByRoomId(roomId).orElseThrow(() ->
                new MessageDeliveryException(BAD_MESSAGE.toString()));

        return byRoomId.getJoinedMembers().contains(new MemberInfo(memberId));
    }

    public boolean onboardCheck(String roomId) throws MessageDeliveryException {
        Room byRoomId = roomRepository.findByRoomId(roomId).orElseThrow(() ->
                new MessageDeliveryException(BAD_MESSAGE.toString()));

        return byRoomId.isOnboard();
    }
}
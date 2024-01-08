package socketTest.socketTestspring.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import socketTest.socketTestspring.domain.ChatRoom;
import socketTest.socketTestspring.repository.RoomRepository;

import java.util.*;

//Memory chat service

@Slf4j
@Data
@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<ChatRoom> findAllRoom() {
        return roomRepository.findAllRoom();
    }

    public Optional<ChatRoom> findRoomById(String roomId) {
        return roomRepository.findRoomById(roomId);
    }

    public ChatRoom createRoom(String name) {
        String roomId = UUID.randomUUID().toString();

        ChatRoom room = ChatRoom.builder()
                .roomId(roomId)
                .name(name)
                .build();

        return roomRepository.createRoom(room);
    }
}
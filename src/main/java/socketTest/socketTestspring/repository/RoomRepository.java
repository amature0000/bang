package socketTest.socketTestspring.repository;

import socketTest.socketTestspring.domain.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    public List<ChatRoom> findAllRoom();
    public Optional<ChatRoom> findRoomById(String roomId);
    public ChatRoom createRoom(ChatRoom chatRoom);
}

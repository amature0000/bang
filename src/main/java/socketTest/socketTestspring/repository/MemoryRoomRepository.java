package socketTest.socketTestspring.repository;

import socketTest.socketTestspring.domain.ChatRoom;

import java.util.*;

public class MemoryRoomRepository implements RoomRepository{
    final private Map<String, ChatRoom> chatRooms = new LinkedHashMap<>();

    @Override
    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    @Override
    public Optional<ChatRoom> findRoomById(String roomId) {
        return Optional.ofNullable(chatRooms.get(roomId));
    }

    @Override
    public ChatRoom createRoom(ChatRoom chatRoom) {
        String name = chatRoom.getRoomId();
        if(chatRooms.containsKey(name)) {
            return null;
        }
        chatRooms.put(name, chatRoom);
        return chatRoom;
    }
}

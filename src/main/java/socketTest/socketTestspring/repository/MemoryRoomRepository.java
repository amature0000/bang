package socketTest.socketTestspring.repository;

import org.springframework.stereotype.Repository;
import socketTest.socketTestspring.domain.MemberInfo;
import socketTest.socketTestspring.domain.Room;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class MemoryRoomRepository {
    private final Map<String, Room> roomMap = new LinkedHashMap<>();

    public Map<String, Room> findAll() {
        return roomMap;
    }

    public void saveRoom(Room room) {
        roomMap.put(room.getRoomId(), room);
    }

    public Optional<Room> findByRoomId(String roomId) {
        return Optional.ofNullable(roomMap.get(roomId));
    }

    public Optional<Room> deleteByRoomId(String roomId) {
        return Optional.ofNullable(roomMap.remove(roomId));
    }

    public boolean joinRoom(Room room, MemberInfo memberInfo) {
        Set<MemberInfo> joinedMembers = room.getJoinedMembers();
        if(joinedMembers.size() >= Room.MAX_JOINED) return false;
        return joinedMembers.add(memberInfo);
    }
}

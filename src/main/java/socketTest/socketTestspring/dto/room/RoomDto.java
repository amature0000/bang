package socketTest.socketTestspring.dto.room;

import socketTest.socketTestspring.domain.Room;

public record RoomDto(String roomId, String roomName, String ownerMemberId, int joinedMembers) {
    public static RoomDto fromRoom(Room room) {
        return new RoomDto(
                room.getRoomId(),
                room.getRoomName(),
                room.getOwnerMemberId(),
                room.getJoinedMembers().size()
        );
    }
}

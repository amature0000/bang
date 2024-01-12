package socketTest.socketTestspring.dto.room.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import socketTest.socketTestspring.domain.Room;

@Getter
@AllArgsConstructor
public class RoomCreateResponse {
    private String roomId;
    private String roomName;
    private String ownerMemberId;

    public RoomCreateResponse(Room room) {
        this.roomId = room.getRoomId();
        this.roomName = room.getRoomName();
        this.ownerMemberId = room.getOwnerMemberId();
    }
}

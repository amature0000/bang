package socketTest.socketTestspring.dto.room.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import socketTest.socketTestspring.domain.Room;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreateRequest {
    private String roomName;

    public Room toEntity() {
        return new Room(this.roomName);
    }
}

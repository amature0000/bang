package socketTest.socketTestspring.dto.room.delete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomDeleteRequest {
    private String roomId;
    private String token;
}

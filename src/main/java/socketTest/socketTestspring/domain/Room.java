package socketTest.socketTestspring.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String roomId; // 채팅방 아이디

    @Column(nullable = false)
    private String roomName; // 채팅방 이름

    public Room(String roomName){
        this.roomName = roomName;
        this.roomId = UUID.randomUUID().toString();
    }
}


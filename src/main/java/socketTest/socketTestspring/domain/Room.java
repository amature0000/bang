package socketTest.socketTestspring.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Room {
    final public static int MAX_JOINED = 7; // 방 최대 인원
    private final String roomId; // 채팅방 아이디
    private final String roomName; // 채팅방 이름
    private final String ownerMemberId;
    @Setter
    private boolean onboard;
    private final Set<MemberInfo> joinedMembers;

    public Room(String roomName, String memberId){
        this.roomName = roomName;
        this.roomId = UUID.randomUUID().toString();
        this.ownerMemberId = memberId;
        this.onboard = false;
        this.joinedMembers = new HashSet<>();
    }
}
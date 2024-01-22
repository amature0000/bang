package socketTest.socketTestspring.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
//TODO : memory domain 으로 변경 후 Map 추가. connected member 관리

@Getter
@Setter
public class Room {
    public static int MAX_JOINED = 7; // 방 최대 인원
    private String roomId; // 채팅방 아이디
    private String roomName; // 채팅방 이름
    private String ownerMemberId;
    private Set<MemberInfo> joinedMembers;

    public Room(String roomName, String memberId){
        this.roomName = roomName;
        this.roomId = UUID.randomUUID().toString();
        this.ownerMemberId = memberId;
        this.joinedMembers = new HashSet<>();
    }
}
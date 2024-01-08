package socketTest.socketTestspring.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

//유저가 접속하는 방의 자료구조

@Getter
@Setter
@Builder
public class ChatRoomDto {
    final private String roomId; // 채팅방 아이디
    final private String name; // 채팅방 이름
    final private int userCount; // 접속자 수
}
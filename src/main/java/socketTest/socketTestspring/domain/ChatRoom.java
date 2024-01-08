package socketTest.socketTestspring.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

//유저가 접속하는 방

@Getter
@Setter
public class ChatRoom {
    final private String roomId; // 채팅방 아이디
    final private String name; // 채팅방 이름
    final private Set<WebSocketSession> sessions = new HashSet<>(); // 접속한 유저들의 연결 정보

    @Builder
    public ChatRoom(String roomId, String name){
        this.roomId = roomId;
        this.name = name;
    }

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public int userCount() {
        return sessions.size();
    }
}


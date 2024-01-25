package socketTest.socketTestspring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.MemberRole;
import socketTest.socketTestspring.domain.Room;
import socketTest.socketTestspring.dto.room.create.RoomCreateRequest;
import socketTest.socketTestspring.dto.room.create.RoomCreateResponse;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteRequest;
import socketTest.socketTestspring.dto.room.delete.RoomDeleteResponse;
import socketTest.socketTestspring.dto.room.join.RoomJoinRequest;
import socketTest.socketTestspring.dto.room.join.RoomJoinResponse;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRoomServiceTest {
    @Autowired
    RoomService roomService;

    @BeforeEach
    void setUp() {
        UserDetails user = createUserDetails();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

    private UserDetails createUserDetails() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(MemberRole.ROLE_USER.name()));
        return new User("test id", "", authorities);
    }
    @Test
    void 방생성() {
        // given
        RoomCreateRequest roomCreateRequest = new RoomCreateRequest("room name");
        // when
        RoomCreateResponse roomCreateResponse = roomService.createRoom(roomCreateRequest);
        // then
        Room room = roomService.findOne(roomCreateResponse.roomId());
        assertThat(room.getRoomName()).isEqualTo("room name");
    }
    @Test
    void 방삭제() {
        //init
        RoomCreateRequest roomCreateRequest = new RoomCreateRequest("room name");
        RoomCreateResponse roomCreateResponse = roomService.createRoom(roomCreateRequest);
        String roomId = roomCreateResponse.roomId();
        //given
        RoomDeleteRequest roomDeleteRequest = new RoomDeleteRequest(roomId);
        //when
        RoomDeleteResponse roomDeleteResponse = roomService.deleteRoom(roomDeleteRequest);
        //then
        assertThat(roomDeleteResponse.response()).isEqualTo("room deleted");
    }
    @Test
    void 입장테스트() {
        // init
        RoomCreateRequest roomCreateRequest = new RoomCreateRequest("room name");
        RoomCreateResponse roomCreateResponse = roomService.createRoom(roomCreateRequest);
        String roomId = roomCreateResponse.roomId();
        // given
        RoomJoinRequest roomJoinRequest = new RoomJoinRequest(roomId);
        // when
        RoomJoinResponse roomJoinResponse = roomService.joinRoom(roomJoinRequest);
        // then
        assertThat(roomJoinResponse.response()).isEqualTo("joined");
    }
}
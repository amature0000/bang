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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RoomServiceTest {
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
        // when
        boolean result = roomService.joinRoom(roomId);
        // then
        assertThat(result).isTrue();
    }
    @Test
    void 입장체크테스트() {
        // init
        RoomCreateRequest roomCreateRequest = new RoomCreateRequest("room name");
        RoomCreateResponse roomCreateResponse = roomService.createRoom(roomCreateRequest);
        String roomId = roomCreateResponse.roomId();
        roomService.joinRoom(roomId);
        //given
        //when
        //then
        boolean result = roomService.isJoined(roomId);
        assertThat(result).isTrue();
    }
    @Test
    void 입장체크테스트2() {
        // init
        RoomCreateRequest roomCreateRequest = new RoomCreateRequest("room name");
        RoomCreateRequest roomCreateRequest2 = new RoomCreateRequest("room name2");

        RoomCreateResponse roomCreateResponse = roomService.createRoom(roomCreateRequest);
        RoomCreateResponse roomCreateResponse2 = roomService.createRoom(roomCreateRequest2);

        String roomId = roomCreateResponse.roomId();
        String roomId2 = roomCreateResponse2.roomId();

        roomService.joinRoom(roomId);
        //given
        //when
        boolean result = roomService.isJoined(roomId2);
        //then
        assertThat(result).isFalse();
    }
    @Test
    void 퇴장테스트() {
        // init
        RoomCreateRequest roomCreateRequest = new RoomCreateRequest("room name");
        RoomCreateResponse roomCreateResponse = roomService.createRoom(roomCreateRequest);
        String roomId = roomCreateResponse.roomId();
        roomService.joinRoom(roomId);
        //given
        //when
        boolean result = roomService.exitRoom(roomId);
        //then
        assertThat(result).isTrue();
        //when
        boolean result2 = roomService.exitRoom(roomId);
        //then
        assertThat(result2).isFalse();
    }
    @Test
    void 퇴장테스트2() {
        // init
        RoomCreateRequest roomCreateRequest = new RoomCreateRequest("room name");
        RoomCreateRequest roomCreateRequest2 = new RoomCreateRequest("room name2");

        RoomCreateResponse roomCreateResponse = roomService.createRoom(roomCreateRequest);
        RoomCreateResponse roomCreateResponse2 = roomService.createRoom(roomCreateRequest2);

        String roomId = roomCreateResponse.roomId();
        String roomId2 = roomCreateResponse2.roomId();

        roomService.joinRoom(roomId);
        //given
        //when
        boolean result = roomService.exitRoom(roomId2);
        //then
        assertThat(result).isFalse();
    }
}
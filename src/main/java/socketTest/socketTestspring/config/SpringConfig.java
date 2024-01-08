package socketTest.socketTestspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import socketTest.socketTestspring.repository.MemoryRoomRepository;
import socketTest.socketTestspring.repository.RoomRepository;
import socketTest.socketTestspring.service.RoomService;

//DI를 위한 config 파일. service 객체를 생성할 때, 알맞은 repository 구현체를 삽입한다.

@Configuration
public class SpringConfig {
    @Bean
    public RoomService roomService(RoomRepository roomRepository) {
        return new RoomService(roomRepository);
    }

    @Bean
    public RoomRepository roomRepository() {
        return new MemoryRoomRepository();
    }
}

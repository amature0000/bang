package socketTest.socketTestspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import socketTest.socketTestspring.domain.Room;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomId(String roomId);
}

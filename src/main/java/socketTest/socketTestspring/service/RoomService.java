package socketTest.socketTestspring.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.Room;
import socketTest.socketTestspring.repository.RoomRepository;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;

    public Room createRoom(Room room) {
        roomRepository.save(room);
        return room;
    }

    @Transactional
    public String deleteRoom(String roomId) {
        Room deleteRoom = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find any room with this roomId"));
        roomRepository.delete(deleteRoom);
        return "room deleted";
    }
}
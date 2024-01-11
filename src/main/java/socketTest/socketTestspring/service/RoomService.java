package socketTest.socketTestspring.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.Room;
import socketTest.socketTestspring.repository.RoomRepository;

import java.util.*;

//Memory chat service

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;

    public Room createRoom(Room room) {
        roomRepository.save(room);
        return room;
    }

    public String deleteRoom(String roomId) {
        Optional<Room> result = roomRepository.findByRoomId(roomId);
        Room deleteRoom = result.orElseThrow(() -> new EntityNotFoundException("Cannot find any room with this roomId"));
        roomRepository.delete(deleteRoom);
        return "room deleted";
    }
}
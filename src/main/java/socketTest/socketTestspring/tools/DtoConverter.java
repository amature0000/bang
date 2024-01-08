package socketTest.socketTestspring.tools;

import socketTest.socketTestspring.domain.ChatRoom;
import socketTest.socketTestspring.dto.ChatRoomDto;

import java.util.ArrayList;
import java.util.List;

public class DtoConverter {
    public static List<ChatRoomDto> convertToDto(List<ChatRoom> rooms) {
        List<ChatRoomDto> roomDtoList = new ArrayList<>();
        for (ChatRoom room : rooms) {

            ChatRoomDto roomDto = ChatRoomDto.builder()
                    .roomId(room.getRoomId())
                    .name(room.getName())
                    .userCount(room.userCount())
                    .build();

            roomDtoList.add(roomDto);
        }
        return roomDtoList;
    }

    public static ChatRoomDto convertToDto(ChatRoom room) {
        return ChatRoomDto.builder()
                .roomId(room.getRoomId())
                .name(room.getName())
                .userCount(room.userCount())
                .build();
    }

    /**
     * Instead of this method, use String{name} to create new domain Object
     * @deprecated this method is currently not use.
     */
    public static ChatRoom convertToDomain(ChatRoomDto roomDto) {
        return ChatRoom.builder()
                .roomId(roomDto.getRoomId())
                .name(roomDto.getName())
                .build();
    }
}

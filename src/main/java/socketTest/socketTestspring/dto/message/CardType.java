package socketTest.socketTestspring.dto.message;

import socketTest.socketTestspring.dto.message.commandCode.GameCommandCode;

public record CardType(GameCommandCode commandCode, String targetId) {
}

package socketTest.socketTestspring.dto.message;

import socketTest.socketTestspring.dto.message.commandCode.GameCommandCode;

public record GameMessage(CardType type, String sender, String channelId, String data) {
    public record CardType(GameCommandCode commandCode, String targetId) {
    }
}
/* example
{
    "type" : {CardType class},
    "sender" : "senderName",
    "channelId" : "channelId",
    "data" : "data"
}
 */

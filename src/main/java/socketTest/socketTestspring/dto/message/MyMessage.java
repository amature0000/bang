package socketTest.socketTestspring.dto.message;

public record MyMessage(MessageType type, String sender, String channelId, String data) {
    public enum MessageType {
        ENTER, TALK, QUIT
    }
}
/* example
{
    "type" : "ENTER",
    "sender" : "senderName",
    "channelId" : "channelId",
    "data" : "data"
}
 */

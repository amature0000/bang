package socketTest.socketTestspring.dto.message;

import com.fasterxml.jackson.databind.ObjectMapper;

public record MyMessage(MessageType type, String sender, String channelId, String data) {
    public enum MessageType {
        ENTER, TALK, QUIT
    }
    public static String extractChannelId(String jsonString) throws Exception {
        return new ObjectMapper().readTree(jsonString).get("channelId").asText();
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

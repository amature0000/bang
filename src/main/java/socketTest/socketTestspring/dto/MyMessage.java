package socketTest.socketTestspring.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public record MyMessage(MessageType type, String sender, String channelId, String data) {
    public enum MessageType {
        ENTER, TALK, QUIT
    }
    public static MyMessage fromJson(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        MessageType type = MessageType.valueOf(jsonNode.get("type").asText());
        String sender = jsonNode.get("sender").asText();
        String channelId = jsonNode.get("channelId").asText();
        String data = jsonNode.get("data").asText();
        return new MyMessage(type, sender, channelId, data);
    }
}
/* example
{
    "type" : "ENTER",
    "sender"z : "senderName",
    "channelId" : "channelId",
    "data" : "data"
}
 */

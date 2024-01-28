package socketTest.socketTestspring.dto.message;

import com.fasterxml.jackson.databind.ObjectMapper;

public record GameMessage(CardType type, String sender, String channelId, String data) {
    public static String extractChannelId(String jsonString) throws Exception {
        return new ObjectMapper().readTree(jsonString).get("channelId").asText();
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

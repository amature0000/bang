package socketTest.socketTestspring.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    public enum MessageType {
        ENTER, TALK, QUIT
    }

    private MessageType type;
    private String sender;
    private String channelId;
    private String data;
}
/* example
{
    "type" : "ENTER",
    "sender" : "senderName",
    "channelId" : "channelId",
    "data" : "data"
}
 */

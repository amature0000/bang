package socketTest.socketTestspring.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private MessageType type;
    private String sender;
    private String channelId;
    private String data;
}
/*
{
    "type" : "ENTER",
    "sender" : "senderName",
    "channelId" : "channelId",
    "data" : "data"
}
 */

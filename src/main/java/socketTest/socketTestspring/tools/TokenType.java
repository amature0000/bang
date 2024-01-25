package socketTest.socketTestspring.tools;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {
    ACCESS("Access_Token", 60 * 60 * 1000),
    REFRESH("Refresh_Token", 24 * 60 * 60 * 1000);


    private final String header;
    private final long expireTime;
}

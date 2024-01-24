package socketTest.socketTestspring.tools;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {
    ACCESS("Access_Token"),
    REFRESH("Refresh_Token");

    private final String header;
}

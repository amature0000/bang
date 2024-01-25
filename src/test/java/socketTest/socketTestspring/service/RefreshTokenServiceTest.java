package socketTest.socketTestspring.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import socketTest.socketTestspring.domain.RefreshToken;
import socketTest.socketTestspring.dto.TokenDto;
import socketTest.socketTestspring.repository.RefreshTokenRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class RefreshTokenServiceTest {
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    void 토큰신규저장() {
        //given
        String memberId = "id";
        TokenDto tokenDto = new TokenDto("dump", "refreshToken");
        //when
        refreshTokenService.updateRefreshToken(memberId, tokenDto);
        //then
        RefreshToken refreshToken = refreshTokenService.findOne(memberId);
        assertThat(refreshToken.getRefreshToken()).isEqualTo(tokenDto.refreshToken());
    }
    @Test
    void 토큰업데이트() {
        //given
        String memberId = "id";
        TokenDto tokenDto = new TokenDto("dump", "refreshToken");
        TokenDto tokenDto2 = new TokenDto("dump", "newRefreshToken");
        //when
        refreshTokenService.updateRefreshToken(memberId, tokenDto);
        refreshTokenService.updateRefreshToken(memberId, tokenDto2);
        //then
        RefreshToken refreshToken = refreshTokenService.findOne(memberId);
        assertThat(refreshToken.getRefreshToken()).isEqualTo(tokenDto2.refreshToken());
    }
}

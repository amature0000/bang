package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import socketTest.socketTestspring.domain.RefreshToken;
import socketTest.socketTestspring.dto.TokenDto;
import socketTest.socketTestspring.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findOne(String memberId) throws UsernameNotFoundException {
        return refreshTokenRepository.findByMemberId(memberId).orElseThrow(() ->
                new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public void updateRefreshToken(String memberId, TokenDto tokenDto){
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId).orElse(null);

        if(refreshToken != null) {
            refreshTokenRepository.save(refreshToken.updateToken(tokenDto.refreshToken()));
        }
        else {
            RefreshToken newToken = new RefreshToken(tokenDto.refreshToken(), memberId);
            refreshTokenRepository.save(newToken);
        }
    }

}

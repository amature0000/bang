package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import socketTest.socketTestspring.domain.RefreshToken;
import socketTest.socketTestspring.dto.TokenDto;
import socketTest.socketTestspring.repository.RefreshTokenRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;


    public RefreshToken findOne(String memberId) throws UsernameNotFoundException {
        return refreshTokenRepository.findByMemberId(memberId).orElseThrow(() ->
                new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
    public void updateRefreshToken(String memberId, TokenDto tokenDto){
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByMemberId(memberId);

        refreshTokenOptional.ifPresent(existingToken -> {
            refreshTokenRepository.save(existingToken.updateToken(tokenDto.refreshToken()));
        });

        refreshTokenOptional.orElseGet(() -> {
            RefreshToken newToken = new RefreshToken(tokenDto.refreshToken(), memberId);
            refreshTokenRepository.save(newToken);
            return newToken;
        });

    }

}

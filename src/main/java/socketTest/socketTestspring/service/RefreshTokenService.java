package socketTest.socketTestspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import socketTest.socketTestspring.domain.RefreshToken;
import socketTest.socketTestspring.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken getRefreshToken(String memberId) throws UsernameNotFoundException {
        return refreshTokenRepository.findByMemberId(memberId).orElseThrow(() ->
                new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}

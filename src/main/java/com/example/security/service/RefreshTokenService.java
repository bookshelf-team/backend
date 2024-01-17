package com.example.security.service;

import com.example.exception.TokenException;
import com.example.model.RefreshToken;
import com.example.model.User;
import com.example.repository.RefreshTokenRepository;
import com.example.repository.UserRepository;
import com.example.util.TimeProvider;
import com.example.util.UUIDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final UUIDGenerator uuidGenerator;

    private final TimeProvider timeProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(timeProvider.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRefreshToken(uuidGenerator.generate().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(timeProvider.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            log.error("Refresh token is expired: {}", refreshToken.getRefreshToken());
            throw new TokenException(refreshToken.getRefreshToken(), "Refresh token is expired. Please signin.");
        }

        return refreshToken;
    }

    @Transactional
    public boolean deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
        log.info("Refresh token deleted for user with ID: {}", userId);
        return true;
    }
}

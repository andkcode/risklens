package com.risklens.core.service;

import com.risklens.core.entity.RefreshToken;
import com.risklens.core.entity.User;
import com.risklens.core.repository.RefreshTokenRepository;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpiration;

    @Transactional
    public String createRefreshToken(User user) {
        String rawToken = UUID.randomUUID().toString();

        RefreshToken refreshToken =
            RefreshToken.builder()
                .user(user)
                .tokenHash(rawToken)
                .createdAt(OffsetDateTime.now())
                .expiresAt(OffsetDateTime.now().plusNanos(refreshExpiration * 1000000))
                .build();

        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    public Optional<RefreshToken> deleteByToken(String token) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByTokenHash(token);
        refreshTokenOpt.ifPresent(refreshTokenRepository::delete);
        return refreshTokenOpt;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByTokenHash(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().isBefore(OffsetDateTime.now()) || token.getRevokedAt() != null) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }
}

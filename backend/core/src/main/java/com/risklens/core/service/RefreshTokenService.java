package com.risklens.core.service;

import com.risklens.core.entity.RefreshToken;
import com.risklens.core.entity.User;
import com.risklens.core.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  @Value("${jwt.refresh-token.expiration}")
  private long refreshTokenDurationMs;

  @Transactional
  public String createRefreshToken(User user) {
    String rawToken = UUID.randomUUID().toString();

    String hashedToken = sha256Hex(rawToken);

    OffsetDateTime now = OffsetDateTime.now();

    RefreshToken refreshToken =
        RefreshToken.builder()
            .user(user)
            .tokenHash(hashedToken)
            .createdAt(now)
            .expiresAt(now.plusSeconds(refreshTokenDurationMs / 1000))
            .build();

    refreshTokenRepository.save(refreshToken);

    return rawToken;
  }

  @Transactional
  public Optional<RefreshToken> findByToken(String rawToken) {
    String hashedToken = sha256Hex(rawToken);
    return refreshTokenRepository.findByTokenHash(hashedToken);
  }

  @Transactional
  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiresAt().isBefore(OffsetDateTime.now()) || token.getRevokedAt() != null) {
      refreshTokenRepository.delete(token);
      throw new RuntimeException(
          "Refresh token was expired or revoked. Please make a new signin request");
    }
    return token;
  }

  @Transactional
  public void deleteByToken(String rawToken) {
    String hashedToken = sha256Hex(rawToken);
    refreshTokenRepository.findByTokenHash(hashedToken).ifPresent(refreshTokenRepository::delete);
  }

  private String sha256Hex(String base) {
    if (base == null) {
      return null;
    }
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error hashing refresh token", e);
    }
  }
}

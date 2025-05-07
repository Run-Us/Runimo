package org.runimo.runimo.auth.repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.domain.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Profile({"prod", "dev"})
@Repository
@RequiredArgsConstructor
public class DatabaseTokenRepository implements JwtTokenRepository {

  private final RefreshTokenJpaRepository refreshTokenJpaRepository;
  @Value("${jwt.refresh.expiration}")
  private Long refreshTokenExpiryMillis;

  @Override
  public Optional<String> findRefreshTokenByUserId(final Long userId) {
    return refreshTokenJpaRepository.findByUserId(userId)
        .map(RefreshToken::getRefreshToken);
  }

  @Override
  public void saveRefreshTokenWithUserId(final Long userId, final String refreshToken) {
    LocalDateTime REPLACE_CUTOFF_TIME = LocalDateTime.now()
        .minus(refreshTokenExpiryMillis, ChronoUnit.MILLIS);

    RefreshToken updatedRefreshToken = refreshTokenJpaRepository.findByUserIdAfterCutoffTime(userId,
            REPLACE_CUTOFF_TIME)
        .map(existingToken -> {
          existingToken.update(refreshToken);
          return existingToken;
        })
        .orElseGet(() ->
            RefreshToken.of(userId, refreshToken)
        );

    refreshTokenJpaRepository.save(updatedRefreshToken);
  }


}

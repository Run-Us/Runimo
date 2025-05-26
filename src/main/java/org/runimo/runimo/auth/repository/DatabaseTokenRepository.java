package org.runimo.runimo.auth.repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.domain.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Profile({"prod", "dev"})
@Repository
@RequiredArgsConstructor
public class DatabaseTokenRepository implements JwtTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiryMillis;


    /**
     * @param userId 사용자 ID 만료되지않은 refreshToken을 조회합니다.
     */
    @Override
    public Optional<String> findRefreshTokenByUserId(final Long userId) {
        LocalDateTime REPLACE_CUTOFF_TIME = LocalDateTime.now()
            .minus(refreshTokenExpiryMillis, ChronoUnit.MILLIS);
        return refreshTokenJpaRepository.findByUserIdAfterCutoffTime(userId, REPLACE_CUTOFF_TIME)
            .map(RefreshToken::getRefreshToken);
    }

    /**
     * @param userId       사용자 ID
     * @param refreshToken refreshToken refreshToken 엔티티를 UPSERT합니다.
     */
    @Override
    public void saveRefreshTokenWithUserId(final Long userId, final String refreshToken) {

        RefreshToken updatedRefreshToken = refreshTokenJpaRepository.findByUserId(
                userId)
            .map(existingToken -> {
                existingToken.update(refreshToken);
                return existingToken;
            })
            .orElseGet(() ->
                RefreshToken.of(userId, refreshToken)
            );

        refreshTokenJpaRepository.save(updatedRefreshToken);
    }

    /**
     * @param userId 사용자 ID 사용자의 refreshToken을 DELETE 합니다.
     */
    @Override
    @Transactional
    public void deleteRefreshTokenByUserId(Long userId) {
        refreshTokenJpaRepository.deleteByUserId(userId);
    }

}

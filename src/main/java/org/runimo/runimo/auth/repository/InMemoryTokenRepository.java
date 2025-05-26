package org.runimo.runimo.auth.repository;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.runimo.runimo.common.cache.InMemoryCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Slf4j
@Profile({"test", "local"})
@Repository
@RequiredArgsConstructor
public class InMemoryTokenRepository implements JwtTokenRepository {

    private final InMemoryCache<Long, String> refreshTokenCache;
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiry;


    @Override
    public Optional<String> findRefreshTokenByUserId(Long userId) {
        return refreshTokenCache.get(userId);
    }

    @Override
    public void saveRefreshTokenWithUserId(Long userId, String refreshToken) {
        refreshTokenCache.put(userId, refreshToken, Duration.ofMillis(refreshTokenExpiry));
    }

    @Override
    public void deleteRefreshTokenByUserId(Long userId) {
        refreshTokenCache.remove(userId);
    }
}

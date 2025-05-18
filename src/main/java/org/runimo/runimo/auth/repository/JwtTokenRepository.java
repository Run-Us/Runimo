package org.runimo.runimo.auth.repository;

import java.util.Optional;

public interface JwtTokenRepository {

    Optional<String> findRefreshTokenByUserId(Long userId);

    void saveRefreshTokenWithUserId(Long userId, String refreshToken);

    void deleteRefreshTokenByUserId(Long userId);
}

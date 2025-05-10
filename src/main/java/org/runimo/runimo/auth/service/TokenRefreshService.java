package org.runimo.runimo.auth.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.repository.JwtTokenRepository;
import org.runimo.runimo.auth.service.dto.TokenPair;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.service.UserFinder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {

    private final JwtResolver jwtResolver;
    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenFactory jwtTokenFactory;
    private final UserFinder userFinder;

    public void putRefreshToken(String userPublicId, String refreshToken) {
        User user = userFinder.findUserByPublicId(userPublicId)
            .orElseThrow(() -> UserJwtException.of(UserHttpResponseCode.TOKEN_REFRESH_FAIL));
        jwtTokenRepository.saveRefreshTokenWithUserId(user.getId(), refreshToken);
    }

    public TokenPair refreshAccessToken(String refreshToken) {
        String userPublicId;
        try {
            jwtResolver.verifyJwtToken(refreshToken);
            userPublicId = jwtResolver.getUserIdFromJwtToken(refreshToken);
        } catch (Exception e) {
            throw UserJwtException.of(UserHttpResponseCode.TOKEN_REFRESH_FAIL);
        }

        User user = userFinder.findUserByPublicId(userPublicId)
            .orElseThrow(() -> UserJwtException.of(UserHttpResponseCode.TOKEN_REFRESH_FAIL));

        // Check if the refresh token is expired
        String storedToken = jwtTokenRepository.findRefreshTokenByUserId(user.getId())
            .orElseThrow(() -> UserJwtException.of(UserHttpResponseCode.REFRESH_EXPIRED));
        if (!storedToken.equals(refreshToken)) {
            throw UserJwtException.of(UserHttpResponseCode.TOKEN_INVALID);
        }

        String newAccessToken = jwtTokenFactory.generateAccessToken(userPublicId);
        return new TokenPair(newAccessToken, refreshToken);
    }
}

package org.runimo.runimo.auth.service.logout;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.service.TokenRefreshService;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.exception.UserException;
import org.runimo.runimo.user.service.UserFinder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogOutUsecaseImpl implements LogOutUsecase {

    private final UserFinder userFinder;
    private final TokenRefreshService tokenRefreshService;
    private final JwtResolver jwtResolver;

    @Override
    public boolean execute(String accessToken) {
        String userPublicId = jwtResolver.getUserIdFromJwtToken(accessToken);
        User user = userFinder.findUserByPublicId(userPublicId).orElseThrow(() -> UserException.of(
            UserHttpResponseCode.USER_NOT_FOUND));

        try {
            tokenRefreshService.getStoredRefreshToken(user.getId());
        } catch (UserJwtException ue) {
            if (ue.getErrorCode() == UserHttpResponseCode.REFRESH_EXPIRED) {
                return false;
            }
        }

        tokenRefreshService.removeRefreshToken(user.getId());
        return true;
    }
}

package org.runimo.runimo.auth.service.logout;

import lombok.RequiredArgsConstructor;
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

    @Override
    public void execute(String accessToken) {
        String userPublicId = tokenRefreshService.getUserPublicIdFromJwt(accessToken);
        User user = userFinder.findUserByPublicId(userPublicId).orElseThrow(() -> UserException.of(
            UserHttpResponseCode.USER_NOT_FOUND));

        tokenRefreshService.removeRefreshToken(user.getId());
    }
}

package org.runimo.runimo.user.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.DevicePlatform;
import org.runimo.runimo.user.domain.LovePoint;
import org.runimo.runimo.user.domain.OAuthInfo;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.domain.UserDeviceToken;
import org.runimo.runimo.user.repository.LovePointRepository;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.runimo.runimo.user.repository.UserDeviceTokenRepository;
import org.runimo.runimo.user.repository.UserRepository;
import org.runimo.runimo.user.service.dto.command.UserCreateCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCreator {

    private final UserRepository userRepository;
    private final OAuthInfoRepository oAuthInfoRepository;
    private final LovePointRepository lovePointRepository;
    private final UserDeviceTokenRepository userDeviceTokenRepository;

    @Transactional
    public User createUser(UserCreateCommand command) {
        User user = User.builder()
            .nickname(command.nickname())
            .imgUrl(command.imgUrl())
            .gender(command.gender())
            .build();
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public OAuthInfo createUserOAuthInfo(User user, SocialProvider provider, String providerId) {
        OAuthInfo oAuthInfo = OAuthInfo.builder()
            .user(user)
            .provider(provider)
            .providerId(providerId)
            .build();
        return oAuthInfoRepository.save(oAuthInfo);
    }

    @Transactional
    public LovePoint createLovePoint(Long userId) {
        LovePoint lovePoint = LovePoint.builder()
            .userId(userId)
            .amount(0L)
            .build();
        return lovePointRepository.save(lovePoint);
    }

    @Transactional
    public UserDeviceToken createUserDeviceToken(User user, String deviceToken,
        DevicePlatform platform) {
        UserDeviceToken userDeviceToken = UserDeviceToken.builder()
            .user(user)
            .deviceToken(deviceToken)
            .platform(platform)
            .notificationAllowed(true)
            .build();
        return userDeviceTokenRepository.save(userDeviceToken);
    }
}

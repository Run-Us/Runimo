package org.runimo.runimo.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.LovePoint;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.domain.UserDeviceToken;
import org.runimo.runimo.user.repository.LovePointRepository;
import org.runimo.runimo.user.repository.UserDeviceTokenRepository;
import org.runimo.runimo.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserFinder {

    private final UserRepository userRepository;
    private final LovePointRepository lovePointRepository;
    private final UserDeviceTokenRepository userDeviceTokenRepository;

    @Transactional(readOnly = true)
    public Optional<User> findUserByPublicId(final String publicId) {
        return userRepository.findByPublicId(publicId);
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserById(final Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public Optional<LovePoint> findLovePointByUserId(Long userId) {
        return lovePointRepository.findLovePointByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Optional<UserDeviceToken> findUserDeviceTokenByUserId(Long userId) {
        return userDeviceTokenRepository.findByUserId(userId);
    }
}

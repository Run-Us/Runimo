package org.runimo.runimo.user.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.exceptions.SignUpException;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.runimo.runimo.user.service.dto.command.UserCreateCommand;
import org.runimo.runimo.user.service.dto.command.UserRegisterCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserCreator userCreator;
    private final UserItemCreator userItemCreator;
    private final OAuthInfoRepository oAuthInfoRepository;

    @Transactional
    public User registerUser(UserRegisterCommand command) {
        User savedUser = userCreator.createUser(
            new UserCreateCommand(command.nickname(), command.imgUrl(), command.gender()));
        userCreator.createUserOAuthInfo(savedUser, command.socialProvider(), command.providerId());
        userCreator.createLovePoint(savedUser.getId());
        userItemCreator.createAll(savedUser.getId());
        return savedUser;
    }

    public void validateExistingUser(String providerId, SocialProvider socialProvider) {
        if (oAuthInfoRepository.existsByProviderIdAndProviderAndDeletedAtIsNull(
            providerId, socialProvider)) {
            throw new SignUpException(UserHttpResponseCode.SIGNIN_FAIL_ALREADY_EXIST);
        }
    }
}

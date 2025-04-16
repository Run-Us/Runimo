package org.runimo.runimo.user.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.rewards.service.eggs.EggGrantService;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.dtos.command.UserCreateCommand;
import org.runimo.runimo.user.service.dtos.command.UserRegisterCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserCreator userCreator;
    private final UserItemCreator userItemCreator;
    private final EggGrantService eggGrantService;

    @Transactional
    public User registerUser(UserRegisterCommand command) {
        User savedUser = userCreator.createUser(
            new UserCreateCommand(command.nickname(), command.imgUrl(), command.gender()));
        userCreator.createUserOAuthInfo(savedUser, command.socialProvider(), command.providerId());
        userCreator.createLovePoint(savedUser.getId());
        userItemCreator.createAll(savedUser.getId());
        eggGrantService.grantGreetingEggToUser(savedUser);
        return savedUser;
    }
}

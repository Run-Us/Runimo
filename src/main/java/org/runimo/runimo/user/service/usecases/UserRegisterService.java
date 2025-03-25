package org.runimo.runimo.user.service.usecases;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.rewards.service.eggs.EggGrantService;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserCreator;
import org.runimo.runimo.user.service.UserItemCreator;
import org.runimo.runimo.user.service.dtos.UserSignupCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserRegisterService {

  private final UserCreator userCreator;
  private final UserItemCreator userItemCreator;
  private final EggGrantService eggGrantService;

  @Transactional
  public User register(UserSignupCommand command, SocialProvider provider, String providerId) {
    User savedUser = userCreator.createUser(command);
    userCreator.createUserOAuthInfo(savedUser, provider, providerId);
    userItemCreator.createAll(savedUser.getId());
    eggGrantService.grantGreetingEggToUser(savedUser);
    return savedUser;
  }
}

package org.runimo.runimo.user.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.OAuthInfo;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.runimo.runimo.user.repository.UserRepository;
import org.runimo.runimo.user.service.dtos.UserSignupCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCreator {
  private final UserRepository userRepository;
  private final OAuthInfoRepository oAuthInfoRepository;

  @Transactional
  public User createUser(UserSignupCommand command) {
    User user = User.builder()
        .nickname(command.nickname())
        .imgUrl(command.imgUrl())
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
}

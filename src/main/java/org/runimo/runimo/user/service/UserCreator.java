package org.runimo.runimo.user.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.*;
import org.runimo.runimo.user.repository.LovePointRepository;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.runimo.runimo.user.repository.UserRepository;
import org.runimo.runimo.user.service.dtos.UserCreateCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCreator {
  private final UserRepository userRepository;
  private final OAuthInfoRepository oAuthInfoRepository;
  private final LovePointRepository lovePointRepository;

  @Transactional
  public User createUser(UserCreateCommand command) {
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

  @Transactional
  public LovePoint createLovePoint(Long userId) {
    LovePoint lovePoint = LovePoint.builder()
        .userId(userId)
        .amount(0L)
        .build();
    return lovePointRepository.save(lovePoint);
  }
}

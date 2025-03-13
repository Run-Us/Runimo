package org.runimo.runimo.user.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserFinder {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public Optional<User> findUserByPublicId(final String publicId) {
    return userRepository.findByPublicId(publicId);
  }

  @Transactional(readOnly = true)
  public Optional<User> findUserById(final Long userId) {
    return userRepository.findById(userId);
  }
}

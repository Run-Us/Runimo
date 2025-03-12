package org.runimo.runimo.user.service.usecases;

import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.service.dtos.SignupUserInfo;
import org.runimo.runimo.user.service.dtos.TokenPair;
import org.runimo.runimo.user.service.dtos.UserSignupCommand;

public interface UserOAuthUsecase {
  TokenPair validateAndLogin(final String rawToken, final SocialProvider provider);

  SignupUserInfo validateAndSignup(final UserSignupCommand command, final String newToken, final SocialProvider socialProvider);
}

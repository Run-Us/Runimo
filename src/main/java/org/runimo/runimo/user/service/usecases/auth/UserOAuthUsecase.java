package org.runimo.runimo.user.service.usecases.auth;

import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.service.dtos.AuthResponse;
import org.runimo.runimo.user.service.dtos.SignupUserResponse;
import org.runimo.runimo.user.service.dtos.UserSignupCommand;

public interface UserOAuthUsecase {
  AuthResponse validateAndLogin(final String rawToken, final SocialProvider provider);

  SignupUserResponse validateAndSignup(final UserSignupCommand command, final String newToken, final SocialProvider socialProvider);
}

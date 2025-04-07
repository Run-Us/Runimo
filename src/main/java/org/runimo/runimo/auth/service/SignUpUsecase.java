package org.runimo.runimo.auth.service;

import org.runimo.runimo.auth.service.dtos.SignupUserResponse;
import org.runimo.runimo.auth.service.dtos.UserSignupCommand;

public interface SignUpUsecase {
  SignupUserResponse register(UserSignupCommand command);
}

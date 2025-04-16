package org.runimo.runimo.auth.service;

import org.runimo.runimo.auth.service.dto.SignupUserResponse;
import org.runimo.runimo.auth.service.dto.UserSignupCommand;

public interface SignUpUsecase {

    SignupUserResponse register(UserSignupCommand command);
}

package org.runimo.runimo.auth.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.jwt.RegisterTokenPayload;
import org.runimo.runimo.auth.service.dtos.SignupUserResponse;
import org.runimo.runimo.auth.service.dtos.UserSignupCommand;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserRegisterService;
import org.runimo.runimo.user.service.dtos.UserRegisterCommand;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpUsecaseImpl implements SignUpUsecase {

    private final UserRegisterService userRegisterService;
    private final JwtResolver jwtResolver;
    private final JwtTokenFactory jwtTokenFactory;

    @Override
    public SignupUserResponse register(UserSignupCommand command) {
        RegisterTokenPayload payload = jwtResolver.getRegisterTokenPayload(command.registerToken());
        User savedUser = userRegisterService.registerUser(new UserRegisterCommand(
            command.nickname(),
            command.imgUrl(),
            command.gender(),
            payload.providerId(),
            payload.socialProvider())
        );
        return new SignupUserResponse(savedUser, jwtTokenFactory.generateTokenPair(savedUser));
    }
}

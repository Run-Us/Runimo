package org.runimo.runimo.auth.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.domain.SignupToken;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.jwt.SignupTokenPayload;
import org.runimo.runimo.auth.service.dto.SignupUserResponse;
import org.runimo.runimo.auth.service.dto.UserSignupCommand;
import org.runimo.runimo.external.FileStorageService;
import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.rewards.service.eggs.EggGrantService;
import org.runimo.runimo.user.domain.AppleUserToken;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.repository.AppleUserTokenRepository;
import org.runimo.runimo.user.service.UserRegisterService;
import org.runimo.runimo.user.service.dto.command.DeviceTokenDto;
import org.runimo.runimo.user.service.dto.command.UserRegisterCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SignUpUsecaseImpl implements SignUpUsecase {

    private final UserRegisterService userRegisterService;
    private final FileStorageService fileStorageService;
    private final EggGrantService eggGrantService;
    private final JwtTokenFactory jwtTokenFactory;
    private final AppleUserTokenRepository appleUserTokenRepository;
    private final SignupTokenService signupTokenService;

    @Override
    @Transactional
    public SignupUserResponse register(UserSignupCommand command) {
        // 1. 토큰 검증
        SignupTokenPayload payload = signupTokenService.extractPayload(command.registerToken());
        SignupToken signupToken = signupTokenService.findUnExpiredToken(payload.token());
        // 2. 유저생성
        userRegisterService.validateExistingUser(payload.providerId(), payload.socialProvider());
        String imgUrl = fileStorageService.storeFile(command.profileImage());
        User savedUser = userRegisterService.registerUser(
            mapToUserCreateCommand(payload, imgUrl, command));
        // 3. 애플 토큰 생성
        if (payload.socialProvider() == SocialProvider.APPLE) {
            createAppleUserToken(savedUser.getId(), signupToken);
        }
        // 4. 신규회원 보상 지급
        Egg grantedEgg = eggGrantService.grantGreetingEggToUser(savedUser);
        EggType eggType = grantedEgg.getEggType();

        signupTokenService.invalidateSignupToken(signupToken);
        return new SignupUserResponse(savedUser, jwtTokenFactory.generateTokenPair(savedUser),
            grantedEgg, eggType.getCode());
    }

    private void createAppleUserToken(Long userId, SignupToken signupToken) {
        AppleUserToken appleUserToken = new AppleUserToken(
            userId,
            signupToken.getRefreshToken()
        );
        appleUserTokenRepository.save(appleUserToken);
    }

    private UserRegisterCommand mapToUserCreateCommand(SignupTokenPayload payload, String imgUrl,
        UserSignupCommand command) {
        return new UserRegisterCommand(
            command.nickname(),
            imgUrl,
            command.gender(),
            payload.providerId(),
            payload.socialProvider(),
            DeviceTokenDto.of(command.deviceToken(), command.devicePlatform())
        );
    }
}

package org.runimo.runimo.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.runimo.runimo.auth.domain.SignupToken;
import org.runimo.runimo.auth.exceptions.SignUpException;
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.jwt.SignupTokenPayload;
import org.runimo.runimo.auth.repository.SignupTokenRepository;
import org.runimo.runimo.auth.service.dto.SignupUserResponse;
import org.runimo.runimo.auth.service.dto.UserSignupCommand;
import org.runimo.runimo.external.FileStorageService;
import org.runimo.runimo.item.EggFixtures;
import org.runimo.runimo.rewards.service.eggs.EggGrantService;
import org.runimo.runimo.user.UserFixtures;
import org.runimo.runimo.user.domain.DevicePlatform;
import org.runimo.runimo.user.domain.Gender;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.repository.AppleUserTokenRepository;
import org.runimo.runimo.user.service.UserRegisterService;

class SignUpUsecaseTest {

    @Mock
    private UserRegisterService userRegisterService;
    @Mock
    private FileStorageService fileStorageService;
    @Mock
    private EggGrantService eggGrantService;
    @Mock
    private JwtTokenFactory jwtTokenFactory;
    @Mock
    private SignupTokenRepository signupTokenRepository;
    @Mock
    private AppleUserTokenRepository appleUserTokenRepository;
    @Mock
    private JwtResolver jwtResolver;

    private SignUpUsecase sut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sut = new SignUpUsecaseImpl(
            userRegisterService,
            fileStorageService,
            eggGrantService,
            jwtTokenFactory,
            signupTokenRepository,
            appleUserTokenRepository,
            jwtResolver
        );
    }

    @Test
    void 회원가입_시_환영_알_지급() {
        // given
        String registerToken = "dummy.token.value";
        SignupTokenPayload payload = new SignupTokenPayload(registerToken, "socialId123",
            SocialProvider.KAKAO);

        when(jwtResolver.getSignupTokenPayload(registerToken)).thenReturn(payload);
        when(signupTokenRepository.findByIdAndCreatedAtAfter(eq(registerToken), any()))
            .thenReturn(Optional.of(new SignupToken(
                registerToken, "refresh", "refresh", SocialProvider.KAKAO)));
        when(userRegisterService.registerUser(any())).thenReturn(UserFixtures.getUserWithId(1L));
        when(eggGrantService.grantGreetingEggToUser(any())).thenReturn(
            EggFixtures.createDefaultEgg());
        when(jwtTokenFactory.generateTokenPair(any())).thenReturn(UserFixtures.TEST_TOKEN_PAIR);

        SignupUserResponse response = sut
            .register(new UserSignupCommand(registerToken, "nickname", null, Gender.UNKNOWN,
                "device_token", DevicePlatform.APNS));

        assertEquals(1L, response.userId());
        assertEquals(UserFixtures.TEST_USER_NICKNAME, response.nickname());
        assertEquals(UserFixtures.TEST_USER_IMG_URL, response.imgUrl());
        assertNotNull(response.tokenPair());
        assertEquals(EggFixtures.TEST_EGG_NAME, response.greetingEggName());
        assertEquals(EggFixtures.TEST_EGG_TYPE_NAME, response.greetingEggType());
        assertEquals(EggFixtures.TEST_EGG_IMG_URL, response.greetingEggImgUrl());
    }

    @Test
    void 유저가_이미_존재하면_실패() {
        // given
        String registerToken = "dummy.token.value";
        SignupTokenPayload payload = new SignupTokenPayload(registerToken, "socialId123",
            SocialProvider.KAKAO);

        when(jwtResolver.getSignupTokenPayload(registerToken)).thenReturn(payload);
        when(signupTokenRepository.findByIdAndCreatedAtAfter(eq(registerToken), any()))
            .thenReturn(Optional.of(new SignupToken(
                registerToken, "refresh", "refresh", SocialProvider.KAKAO)));

        doThrow(new org.runimo.runimo.auth.exceptions.SignUpException(
            UserHttpResponseCode.SIGNIN_FAIL_ALREADY_EXIST))
            .when(userRegisterService)
            .validateExistingUser(payload.providerId(), payload.socialProvider());

        assertThrows(SignUpException.class, () -> {
            sut.register(new UserSignupCommand(registerToken, "nickname", null, Gender.UNKNOWN,
                "device_token", DevicePlatform.APNS));
        });
    }
}

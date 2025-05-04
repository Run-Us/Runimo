package org.runimo.runimo.auth.service;

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
import org.runimo.runimo.auth.jwt.JwtResolver;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.jwt.SignupTokenPayload;
import org.runimo.runimo.auth.repository.SignupTokenRepository;
import org.runimo.runimo.auth.service.dto.UserSignupCommand;
import org.runimo.runimo.external.FileStorageService;
import org.runimo.runimo.user.domain.Gender;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.repository.AppleUserTokenRepository;
import org.runimo.runimo.user.service.UserRegisterService;

class SignUpUsecaseTest {

  @Mock
  private UserRegisterService userRegisterService;
  @Mock
  private FileStorageService fileStorageService;
  @Mock
  private JwtTokenFactory jwtTokenFactory;
  @Mock
  private SignupTokenRepository signupTokenRepository;
  @Mock
  private AppleUserTokenRepository appleUserTokenRepository;
  @Mock
  private JwtResolver jwtResolver;

  private SignUpUsecaseImpl sut;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    sut = new SignUpUsecaseImpl(
        userRegisterService,
        fileStorageService,
        jwtTokenFactory,
        signupTokenRepository,
        appleUserTokenRepository,
        jwtResolver
    );
  }

  @Test
  void 유저가_이미_존재하면_실패() {
    // given
    String registerToken = "dummy.token.value";
    SignupTokenPayload payload = new SignupTokenPayload(registerToken, "socialId123",
        SocialProvider.KAKAO);

    when(jwtResolver.getSignupTokenPayload(registerToken)).thenReturn(payload);
    when(signupTokenRepository.findByIdAndCreatedAtAfter(eq(registerToken), any()))
        .thenReturn(Optional.of(new org.runimo.runimo.auth.domain.SignupToken(
            registerToken, "refresh", "refresh", SocialProvider.KAKAO)));

    doThrow(new org.runimo.runimo.auth.exceptions.SignUpException(
        org.runimo.runimo.user.enums.UserHttpResponseCode.SIGNIN_FAIL_ALREADY_EXIST))
        .when(userRegisterService)
        .validateExistingUser(payload.providerId(), payload.socialProvider());

    assertThrows(org.runimo.runimo.auth.exceptions.SignUpException.class, () -> {
      sut.register(new UserSignupCommand(registerToken, "nickname", null, Gender.UNKNOWN));
    });
  }
}

package org.runimo.runimo.user.service.usecases.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.service.OidcNonceService;
import org.runimo.runimo.auth.service.OidcService;
import org.runimo.runimo.user.domain.OAuthInfo;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.enums.UserHttpResponseCode;
import org.runimo.runimo.user.exceptions.SignUpException;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.runimo.runimo.user.service.dtos.AuthResponse;
import org.runimo.runimo.user.service.dtos.SignupUserResponse;
import org.runimo.runimo.user.service.dtos.TokenPair;
import org.runimo.runimo.user.service.dtos.UserSignupCommand;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserOAuthUsecaseImpl implements UserOAuthUsecase {
  private final JwtTokenFactory jwtfactory;
  private final OidcService oidcService;
  private final OidcNonceService oidcNonceService;
  private final OAuthInfoRepository oAuthInfoRepository;
  private final UserRegisterService userRegisterService;

  @Override
  @Transactional
  public AuthResponse validateAndLogin(final String rawToken, final SocialProvider provider) {
    DecodedJWT token = JWT.decode(rawToken);
    String pid = oidcService.validateOidcTokenAndGetProviderId(token, provider);
    OAuthInfo oAuthInfo = oAuthInfoRepository.findByProviderAndProviderId(provider, pid)
        .orElseThrow(() -> new SignUpException(UserHttpResponseCode.LOGIN_FAIL_NOT_SIGN_IN));
    TokenPair tokenPair = jwtfactory.generateTokenPair(oAuthInfo.getUser());
    return new AuthResponse(oAuthInfo.getUser(), tokenPair);
  }

  @Override
  @Transactional
  public SignupUserResponse validateAndSignup(final UserSignupCommand command, final String rawToken, SocialProvider provider) {
    DecodedJWT token = JWT.decode(rawToken);
    String pid = oidcService.validateOidcTokenAndGetProviderId(token, provider);
    oAuthInfoRepository.findByProviderAndProviderId(provider, pid)
        .ifPresent(oAuthInfo -> {
          throw new SignUpException(UserHttpResponseCode.SIGNIN_FAIL_ALREADY_EXIST);
        });
    User savedUser = userRegisterService.register(command, pid);
    TokenPair tokenPair = jwtfactory.generateTokenPair(savedUser);
    return new SignupUserResponse(savedUser, tokenPair);
  }
}

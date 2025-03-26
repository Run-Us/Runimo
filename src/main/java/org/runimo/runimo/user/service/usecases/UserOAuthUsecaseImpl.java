package org.runimo.runimo.user.service.usecases;

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
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.runimo.runimo.user.service.dtos.SignupUserInfo;
import org.runimo.runimo.user.service.dtos.TokenPair;
import org.runimo.runimo.user.service.dtos.UserSignupCommand;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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
  public TokenPair validateAndLogin(final String rawToken, final SocialProvider provider) {
    DecodedJWT token = JWT.decode(rawToken);
    String pid = oidcService.validateOidcTokenAndGetProviderId(token, provider);
    OAuthInfo oAuthInfo = oAuthInfoRepository.findByProviderAndProviderId(provider, pid)
        .orElseThrow(() -> new NoSuchElementException("가입된 유저 없음."));
    oidcNonceService.useNonce(token, provider);
    return jwtfactory.generateTokenPair(oAuthInfo.getUser());
  }

  @Override
  @Transactional
  public SignupUserInfo validateAndSignup(final UserSignupCommand command, final String rawToken, SocialProvider provider) {
    DecodedJWT token = JWT.decode(rawToken);
    String pid = oidcService.validateOidcTokenAndGetProviderId(token, provider);
    oAuthInfoRepository.findByProviderAndProviderId(provider, pid)
        .ifPresent(oAuthInfo -> {
          throw new IllegalArgumentException();
        });
    User savedUser = userRegisterService.register(command, pid);
    return new SignupUserInfo(savedUser.getId(), jwtfactory.generateTokenPair(savedUser));
  }
}

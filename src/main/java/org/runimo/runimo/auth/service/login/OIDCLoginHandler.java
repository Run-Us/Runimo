package org.runimo.runimo.auth.service.login;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.domain.SignupToken;
import org.runimo.runimo.auth.jwt.JwtTokenFactory;
import org.runimo.runimo.auth.repository.SignupTokenRepository;
import org.runimo.runimo.auth.service.TokenRefreshService;
import org.runimo.runimo.auth.service.dto.AuthResult;
import org.runimo.runimo.auth.service.dto.AuthStatus;
import org.runimo.runimo.auth.service.dto.TokenPair;
import org.runimo.runimo.user.domain.OAuthInfo;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.repository.OAuthInfoRepository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public abstract class OIDCLoginHandler implements SocialLoginHandler {

    protected final JwtTokenFactory jwtTokenFactory;
    protected final TokenRefreshService tokenRefreshService;
    protected final OAuthInfoRepository oAuthInfoRepository;
    protected final SignupTokenRepository signupTokenRepository;

    @Override
    @Transactional
    public AuthResult validateAndLogin(Object... params) {
        SocialProvider provider = getProviderType();
        String providerId = extractProviderId(params);
        return oAuthInfoRepository.findByProviderAndProviderId(provider, providerId)
            .map(this::handleLogin)
            .orElseGet(() -> handleSignup(providerId, provider));
    }

    ///  Modifiable functions
    protected abstract String extractProviderId(Object... params);

    protected abstract SignupToken createProviderSpecificToken(String tokenId, String providerId,
        Object... params);

    protected abstract SocialProvider getProviderType();

    ///  Template Functions
    protected final AuthResult handleLogin(OAuthInfo oAuthInfo) {
        User user = oAuthInfo.getUser();
        TokenPair tokenPair = jwtTokenFactory.generateTokenPair(user);
        tokenRefreshService.putRefreshToken(user.getPublicId(), tokenPair.refreshToken());
        return AuthResult.success(AuthStatus.LOGIN_SUCCESS, user, tokenPair);
    }

    protected final AuthResult handleSignup(String providerId, Object... params) {
        String signupToken = createTemporalSignupToken(providerId, params);
        return AuthResult.signupNeeded(AuthStatus.SIGNUP_NEEDED, signupToken);
    }

    protected final String createTemporalSignupToken(String providerId, Object... params) {
        String temporalTokenId = UUID.randomUUID().toString();

        SignupToken signupToken = createProviderSpecificToken(
            temporalTokenId,
            providerId,
            params
        );

        signupTokenRepository.save(signupToken);

        return jwtTokenFactory.generateSignupTemporalToken(
            providerId,
            getProviderType(),
            temporalTokenId
        );
    }
}

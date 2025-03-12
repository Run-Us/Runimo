package org.runimo.runimo.auth.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.auth.jwt.TokenStatus;
import org.runimo.runimo.auth.repository.OAuthTokenRepository;
import org.runimo.runimo.user.domain.SocialProvider;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OidcNonceService {

  private final OAuthTokenRepository oAuthTokenRepository;
  private final static String NONCE_CLAIM_KEY = "nonce";

  public void checkNonceAndSave(final SocialProvider provider, final DecodedJWT decodedJWT) {
    Optional<TokenStatus> existingOidcTokenEntry = oAuthTokenRepository.getNonceStatus(
        provider,
        decodedJWT.getSubject(),
        decodedJWT.getClaim(NONCE_CLAIM_KEY).asString()
    );
    validateEntryExistsAndUnUsed(existingOidcTokenEntry);
    storeNonceWithTTL(provider, decodedJWT, getTTLForNonce(decodedJWT));
  }

  private Duration getTTLForNonce(DecodedJWT decodedJWT) {
    return Duration.between(Instant.now(), decodedJWT.getExpiresAtAsInstant());
  }

  /**
   * Nonce가 없거나 이미 사용되었다면 탈취의 위험이 있으므로 에러를 반환
   */
  private void validateEntryExistsAndUnUsed(Optional<TokenStatus> status) {
    if (status.isEmpty()) {
      throw new IllegalStateException("nonce is not found");
    }
    if (status.get() == TokenStatus.USED) {
      throw new IllegalStateException("nonce is already used");
    }
  }

  private void storeNonceWithTTL(final SocialProvider provider, final DecodedJWT decodedJWT, final Duration ttl) {
    oAuthTokenRepository.storeNonce(
        provider,
        decodedJWT.getSubject(),
        String.valueOf(decodedJWT.getClaim(NONCE_CLAIM_KEY)),
        TokenStatus.PENDING,
        ttl
    );
  }

  public void useNonce(DecodedJWT token, SocialProvider provider) {
    oAuthTokenRepository.updateNonceStatus(provider, token.getSubject(), String.valueOf(token.getClaim(NONCE_CLAIM_KEY)), TokenStatus.USED);
  }
}

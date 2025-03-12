package org.runimo.runimo.auth.repository;

import org.runimo.runimo.auth.jwt.TokenStatus;
import org.runimo.runimo.user.domain.SocialProvider;

import java.time.Duration;
import java.util.Optional;

public interface OAuthTokenRepository {

  void storeNonce(
      SocialProvider socialProvider, String sub, String nonce,
      TokenStatus status, Duration ttl
  );

  Optional<TokenStatus> getNonceStatus(SocialProvider socialProvider, String sub, String nonce);

  void updateNonceStatus(SocialProvider socialProvider, String sub, String nonce, TokenStatus status);
}

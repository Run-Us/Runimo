package org.runimo.runimo.user.repository;

import org.runimo.runimo.user.domain.OAuthInfo;
import org.runimo.runimo.user.domain.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthInfoRepository extends JpaRepository<OAuthInfo, Long> {

  @Query("SELECT o FROM OAuthInfo o WHERE o.provider = :provider AND o.providerId = :providerId")
  Optional<OAuthInfo> findByProviderAndProviderId(SocialProvider provider, String providerId);
}

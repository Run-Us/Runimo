package org.runimo.runimo.user.repository;

import java.util.Optional;
import org.runimo.runimo.user.domain.OAuthInfo;
import org.runimo.runimo.user.domain.SocialProvider;
import org.runimo.runimo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthInfoRepository extends JpaRepository<OAuthInfo, Long> {

    @Query("""
          SELECT o FROM OAuthInfo o
                  WHERE o.deletedAt is null AND o.provider = :provider AND o.providerId = :providerId
        """)
    Optional<OAuthInfo> findByProviderAndProviderId(SocialProvider provider, String providerId);

    SocialProvider user(User user);

    @Query("""
        select o from OAuthInfo o 
                where o.deletedAt is null AND o.user.id = :userId
        """)
    Optional<OAuthInfo> findByUserId(Long userId);
}

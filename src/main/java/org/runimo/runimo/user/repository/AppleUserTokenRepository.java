package org.runimo.runimo.user.repository;

import java.util.Optional;
import org.runimo.runimo.user.domain.AppleUserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppleUserTokenRepository extends JpaRepository<AppleUserToken, Long> {

    Optional<AppleUserToken> findByUserId(Long id);
}
package org.runimo.runimo.auth.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.runimo.runimo.auth.domain.RefreshToken;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Profile({"prod", "dev"})
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByUserId(Long id);

    @Query("select distinct r from RefreshToken r " +
        "where r.userId = :userId and r.updatedAt > :cutOffDateTime")
    Optional<RefreshToken> findByUserIdAfterCutoffTime(Long userId, LocalDateTime cutOffDateTime);
}

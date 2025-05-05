package org.runimo.runimo.auth.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.runimo.runimo.auth.domain.SignupToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SignupTokenRepository extends JpaRepository<SignupToken, String> {

    @Query(
        "SELECT st FROM SignupToken st WHERE st.token = :token AND st.createdAt > :createdAtAfter"
    )
    Optional<SignupToken> findByIdAndCreatedAtAfter(String token, LocalDateTime createdAtAfter);

    void deleteByToken(String token);
}
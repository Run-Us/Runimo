package org.runimo.runimo.user.repository;

import java.util.Optional;
import org.runimo.runimo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPublicId(final String publicId);
}

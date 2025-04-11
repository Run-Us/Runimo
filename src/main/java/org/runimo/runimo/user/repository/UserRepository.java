package org.runimo.runimo.user.repository;

import java.util.Optional;
import org.runimo.runimo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            select u from User u where u.id = :userId and u.deletedAt is null
        """)
    Optional<User> findById(Long userId);


    @Query(
        """
            select u from User u where u.publicId = :publicId and u.deletedAt is null
            """
    )
    Optional<User> findByPublicId(final String publicId);
}

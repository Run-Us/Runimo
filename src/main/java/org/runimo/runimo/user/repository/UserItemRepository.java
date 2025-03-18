package org.runimo.runimo.user.repository;

import org.runimo.runimo.user.domain.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {

  @Query("select ui from UserItem ui where ui.userId = :userId and ui.itemId = :itemId")
  Optional<UserItem> findByUserIdAndItemId(Long userId, Long itemId);
}

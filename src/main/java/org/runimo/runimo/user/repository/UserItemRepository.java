package org.runimo.runimo.user.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.user.domain.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserItemRepository extends JpaRepository<UserItem, Long> {
  @Query("select ui from UserItem ui where ui.userId = :userId and ui.itemId = :itemId")
  Optional<UserItem> findByUserIdAndItemId(Long userId, Long itemId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
  @Query("select ui from UserItem ui where ui.userId = :userId and ui.itemId = :itemId")
  Optional<UserItem> findByUserIdAndItemIdForUpdate(Long userId, Long itemId);

  @Query("select ui " +
      "from UserItem ui " +
      "join Egg e on ui.itemId = e.id " +
      "where ui.userId = :userId and e.eggType = :eggType")
  Optional<UserItem> findByUserIdAndEggType(Long userId, EggType eggType);

  @Query("select ui from UserItem ui " +
      "join Egg e on ui.itemId = e.id " +
      "where ui.userId = :userId")
  List<UserItem> findAllEggsByUserId(Long userId);
}

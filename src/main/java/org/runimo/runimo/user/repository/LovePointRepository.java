package org.runimo.runimo.user.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.runimo.runimo.user.domain.LovePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface LovePointRepository extends JpaRepository<LovePoint, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
  @Query("select l from LovePoint l where l.userId = :id")
  Optional<LovePoint> findByUserIdWithXLock(Long id);

  Optional<LovePoint> findLovePointByUserId(Long id);
}
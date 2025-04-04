package org.runimo.runimo.records.repository;

import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.service.dtos.DailyStat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<RunningRecord, Long> {
  Optional<RunningRecord> findByRecordPublicId(String id);

  @Query("SELECT r FROM RunningRecord r " +
      "WHERE r.userId = :userId " +
      "AND r.startedAt BETWEEN :startOfWeek AND :now")
  Slice<RunningRecord> findFirstRunOfWeek(
      @Param("userId") Long userId,
      @Param("startOfWeek") LocalDateTime startOfWeek,
      @Param("now") LocalDateTime now,
      Pageable pageable
  );

  @Query("SELECT COUNT(r.id) FROM RunningRecord r WHERE r.userId = :id")
  Long countByUserId(Long id);

  @Query("select r from RunningRecord r where r.userId = :userId")
  Slice<RunningRecord> findLatestByUserId(Long userId, Pageable pageRequest);

  @Query("select new org.runimo.runimo.records.service.dtos.DailyStat(" +
      "cast(r.startedAt as localdate), " +
      "sum(r.totalDistance.amount)) " +
      "from RunningRecord r " +
      "where r.userId = :userId " +
      "and r.startedAt between :startOfWeek and :now " +
      "group by function('date', r.startedAt)")
  List<DailyStat> findDailyDistanceByUserIdAndThisWeek(Long userId, LocalDateTime startOfWeek, LocalDateTime now);

}

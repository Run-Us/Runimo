package org.runimo.runimo.records.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.service.dto.RecordStatDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query("select new org.runimo.runimo.records.service.dto.RecordStatDto(" +
        "r.startedAt, " +
        "r.endAt, " +
        "r.totalDistance.amount) " +
        "from RunningRecord r " +
        "where r.userId = :userId " +
        "and r.startedAt between :startOfWeek and :now " +
        "order by r.startedAt asc")
    List<RecordStatDto> findRecordStatByUserIdAndBetween(Long userId, LocalDateTime startOfWeek,
        LocalDateTime now);

    Page<RunningRecord> findRecordByUserIdOrderByStartedAtDesc(Long id, Pageable pageable);


    @Query("select r " +
        "from RunningRecord r " +
        "where r.userId = :userId " +
        "and r.startedAt between :from and :to")
    Page<RunningRecord> findRecordByUserIdAndBetween(Long userId, LocalDateTime from,
        LocalDateTime to, Pageable pageRequest);
}

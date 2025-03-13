package org.runimo.runimo.records.repository;

import org.runimo.runimo.records.domain.RunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<RunningRecord, Long> {
  Optional<RunningRecord> findByRecordPublicId(String id);
}

package org.runimo.runimo.records.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.repository.RecordRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecordFinder {

  private final RecordRepository recordRepository;

  @Transactional(readOnly = true)
  public Optional<RunningRecord> findById(Long id) {
    return recordRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public Optional<RunningRecord> findByPublicId(String id) {
    return recordRepository.findByRecordPublicId(id);
  }
}

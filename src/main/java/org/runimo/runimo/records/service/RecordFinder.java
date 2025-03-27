package org.runimo.runimo.records.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.repository.RecordRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

  @Transactional(readOnly = true)
  public Optional<RunningRecord> findFirstRunOfCurrentWeek(Long userId) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1L).withHour(0).withMinute(0).withSecond(0);
    PageRequest pageRequest = PageRequest.of(0, 1, Sort.by("createdAt").ascending());
    return recordRepository.findFirstRunOfWeek(userId, startOfWeek, now, pageRequest).stream().findFirst();
  }
}

package org.runimo.runimo.records.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.repository.RecordRepository;
import org.runimo.runimo.records.service.dto.DailyStat;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1L).withHour(0)
            .withMinute(0).withSecond(0);
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by("startedAt").ascending());
        return recordRepository.findFirstRunOfWeek(userId, startOfWeek, now, pageRequest).stream()
            .findFirst();
    }

    @Transactional(readOnly = true)
    public Long countByUserId(Long userId) {
        return recordRepository.countByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Optional<RunningRecord> findLatestRunningRecordByUserId(Long userId) {
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by("startedAt").descending());
        return recordRepository.findLatestByUserId(userId, pageRequest).stream().findFirst();
    }

    @Transactional(readOnly = true)
    public List<DailyStat> findDailyStatByUserIdBetween(Long id, LocalDateTime from,
        LocalDateTime to) {
        return recordRepository.findDailyDistanceByUserIdAndThisWeek(id, from, to);
    }
}

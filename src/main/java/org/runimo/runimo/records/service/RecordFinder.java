package org.runimo.runimo.records.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.response.PageData;
import org.runimo.runimo.common.response.PageInfo;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.repository.RecordRepository;
import org.runimo.runimo.records.service.dto.DailyStat;
import org.runimo.runimo.records.service.dto.RecordSimpleView;
import org.runimo.runimo.records.service.dto.RecordStatDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
  public Optional<RunningRecord> findLatestRunningRecordByUserId(Long userId) {
    PageRequest pageRequest = PageRequest.of(0, 1, Sort.by("startedAt").descending());
    return recordRepository.findLatestByUserId(userId, pageRequest).stream().findFirst();
  }

  @Transactional(readOnly = true)
  public List<DailyStat> findDailyStatByUserIdBetween(Long id, LocalDateTime from,
      LocalDateTime to) {
    return mapToDailyStatList(
        recordRepository.findRecordStatByUserIdAndBetween(id, from, to));
  }

  @Transactional(readOnly = true)
  public PageData<RecordSimpleView> findRecordSimpleViewByUserIdBetween(Long userId, LocalDate from,
      LocalDate to, Pageable pageRequest) {
    Page<RunningRecord> pages = recordRepository.findRecordByUserIdAndBetween(
        userId, from.atStartOfDay(), to.atTime(23, 59, 59), pageRequest);
    return mapToPageData(pages, pageRequest);
  }

  private PageData<RecordSimpleView> mapToPageData(Page<RunningRecord> pages,
      Pageable pageRequest) {
    if (pages.isEmpty()) {
      return new PageData<>(List.of(),
          PageInfo.empty(pageRequest.getPageNumber(), pageRequest.getPageSize()));
    }
    return new PageData<>(
        pages.getContent().stream()
            .map(RecordSimpleView::from)
            .toList(),
        PageInfo.of(pages)
    );
  }

  private List<DailyStat> mapToDailyStatList(List<RecordStatDto> recordStatDtos) {
    List<DailyStat> list = new ArrayList<>();
    for (RecordStatDto recordStatDto : recordStatDtos) {
      if (list.isEmpty() || !list.getLast().getDate().equals(recordStatDto.getStartedAt()
          .toLocalDate())) {
        DailyStat dailyStat = DailyStat.empty(recordStatDto.getStartedAt().toLocalDate());
        dailyStat.addData(recordStatDto);
        list.add(dailyStat);
        continue;
      }
      DailyStat last = list.getLast();
      last.addData(recordStatDto);
    }
    return list;
  }


}

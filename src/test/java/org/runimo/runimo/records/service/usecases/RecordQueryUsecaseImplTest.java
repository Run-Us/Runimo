package org.runimo.runimo.records.service.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.runimo.runimo.records.service.RecordFinder;
import org.runimo.runimo.records.service.dtos.RunningRecordDistance;
import org.runimo.runimo.records.service.dtos.DailyStat;
import org.runimo.runimo.records.service.dtos.WeeklyRecordStatResponse;
import org.runimo.runimo.records.service.dtos.WeeklyStatQuery;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RecordQueryUsecaseImplTest {

  @Mock
  private RecordFinder recordFinder;

  private RecordQueryUsecase queryUsecase;

  @BeforeEach
  void setUp() {
    openMocks(this);
    queryUsecase = new RecordQueryUsecaseImpl(recordFinder);
  }

  @Test
  void 주간_일별_달리기_거리_합() {
    // given
    LocalDateTime now = LocalDateTime.of(2025, 4, 6, 10, 0);
    LocalDateTime startOfWeek = now.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0);

    List<RunningRecordDistance> savedData = List.of(
        new RunningRecordDistance(1000L, LocalDate.of(2025, 3, 31)), // 월
        new RunningRecordDistance(2000L, LocalDate.of(2025, 4, 1)),  // 화
        new RunningRecordDistance(3000L, LocalDate.of(2025, 4, 2)),  // 수
        new RunningRecordDistance(4000L, LocalDate.of(2025, 4, 3)),  // 목
        new RunningRecordDistance(5000L, LocalDate.of(2025, 4, 4)),  // 금
        new RunningRecordDistance(6000L, LocalDate.of(2025, 4, 5)),  // 토
        new RunningRecordDistance(7000L, LocalDate.of(2025, 4, 6))   // 일
    );

    when(recordFinder.findDailyStatByUserIdBetween(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(savedData);

    // when
    WeeklyRecordStatResponse response = queryUsecase.getUserWeeklyRecordStat(new WeeklyStatQuery(
        1L,
        LocalDate.of(2025, 3, 31),
        LocalDate.of(2025, 4, 6)
    ));

    // then
    List<DailyStat> expectedDistances = List.of(
        new DailyStat(LocalDate.of(2025, 3, 31), 1000L),
        new DailyStat(LocalDate.of(2025, 4, 1), 2000L),
        new DailyStat(LocalDate.of(2025, 4, 2), 3000L),
        new DailyStat(LocalDate.of(2025, 4, 3), 4000L),
        new DailyStat(LocalDate.of(2025, 4, 4), 5000L),
        new DailyStat(LocalDate.of(2025, 4, 5), 6000L),
        new DailyStat(LocalDate.of(2025, 4, 6), 7000L)
    );

    assertEquals(expectedDistances, response.dailyStats());
  }

  @Test
  void 주간_일별_달리기_거리_합_데이터가_없는_요일이_있을때() {
    // given
    LocalDateTime now = LocalDateTime.of(2025, 4, 6, 10, 0);
    LocalDateTime startOfWeek = now.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0);

    List<RunningRecordDistance> savedData = List.of(
        new RunningRecordDistance(1000L, LocalDate.of(2025, 3, 31)), // 월
        new RunningRecordDistance(9000L, LocalDate.of(2025, 4, 1)),  // 화
        // 수, 목 데이터 없음
        new RunningRecordDistance(5000L, LocalDate.of(2025, 4, 4)),  // 금
        new RunningRecordDistance(6000L, LocalDate.of(2025, 4, 5)),  // 토
        new RunningRecordDistance(7000L, LocalDate.of(2025, 4, 6))   // 일
    );

    when(recordFinder.findDailyStatByUserIdBetween(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
        .thenReturn(savedData);

    // when
    WeeklyRecordStatResponse response = queryUsecase.getUserWeeklyRecordStat(new WeeklyStatQuery(
        1L,
        LocalDate.of(2025, 3, 31),
        LocalDate.of(2025, 4, 6)
    ));

    // then
    List<DailyStat> expectedDistances = List.of(
        new DailyStat(LocalDate.of(2025, 3, 31), 1000L),
        new DailyStat(LocalDate.of(2025, 4, 1), 9000L),
        new DailyStat(LocalDate.of(2025, 4, 2), 0L),  // 데이터 없음
        new DailyStat(LocalDate.of(2025, 4, 3), 0L),  // 데이터 없음
        new DailyStat(LocalDate.of(2025, 4, 4), 5000L),
        new DailyStat(LocalDate.of(2025, 4, 5), 6000L),
        new DailyStat(LocalDate.of(2025, 4, 6), 7000L)
    );

    assertEquals(expectedDistances, response.dailyStats());
  }
}
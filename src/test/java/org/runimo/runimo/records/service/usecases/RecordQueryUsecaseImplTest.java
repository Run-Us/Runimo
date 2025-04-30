package org.runimo.runimo.records.service.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.runimo.runimo.records.service.RecordFinder;
import org.runimo.runimo.records.service.dto.DailyStat;
import org.runimo.runimo.records.service.dto.WeeklyRecordStatResponse;
import org.runimo.runimo.records.service.dto.WeeklyStatQuery;
import org.runimo.runimo.records.service.usecases.dtos.MonthlyRecordStatResponse;
import org.runimo.runimo.records.service.usecases.dtos.MonthlyStatQuery;

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
        List<DailyStat> savedData = List.of(
            new DailyStat(LocalDate.of(2025, 3, 31), 20L, 1L, 1000L), // 월
            new DailyStat(LocalDate.of(2025, 4, 1), 20L, 1L, 2000L), // 화
            new DailyStat(LocalDate.of(2025, 4, 2), 20L, 1L, 3000L), // 수
            new DailyStat(LocalDate.of(2025, 4, 3), 20L, 1L, 4000L), // 목
            new DailyStat(LocalDate.of(2025, 4, 4), 20L, 1L, 5000L), // 금
            new DailyStat(LocalDate.of(2025, 4, 5), 20L, 1L, 6000L), // 토
            new DailyStat(LocalDate.of(2025, 4, 6), 20L, 1L, 7000L)  // 일
        );
        Long expectedDistanceSum = savedData.stream().map(DailyStat::getDistanceInMeters)
            .reduce(0L, Long::sum);
        Long expectedRunningCount = savedData.stream().map(DailyStat::getRunningCount)
            .reduce(0L, Long::sum);

        when(recordFinder.findDailyStatByUserIdBetween(eq(1L), any(LocalDateTime.class),
            any(LocalDateTime.class)))
            .thenReturn(savedData);

        when(recordFinder.findDailyStatByUserIdBetween(eq(1L), any(LocalDateTime.class),
            any(LocalDateTime.class)))
            .thenReturn(savedData);

        when(recordFinder.findDailyStatByUserIdBetween(eq(1L), any(LocalDateTime.class),
            any(LocalDateTime.class)))
            .thenReturn(savedData);

        // when
        WeeklyRecordStatResponse response = queryUsecase.getUserWeeklyRecordStat(
            new WeeklyStatQuery(
                1L,
                LocalDate.of(2025, 3, 31),
                LocalDate.of(2025, 4, 6)
            ));

        // then
        assertEquals(expectedDistanceSum, response.simpleStat().getTotalDistanceInMeters());
        assertEquals(expectedRunningCount, response.simpleStat().getTotalRunningCount());
    }

    @Test
    void 주간_일별_달리기_거리_합_데이터가_없는_요일이_있을때() {
        // given
        List<DailyStat> savedData = List.of(
            new DailyStat(LocalDate.of(2025, 3, 31), 20L, 1L, 1000L), // 월
            new DailyStat(LocalDate.of(2025, 4, 1), 20L, 3L, 9000L), // 화
            new DailyStat(LocalDate.of(2025, 4, 4), 20L, 1L, 5000L), // 금
            new DailyStat(LocalDate.of(2025, 4, 5), 20L, 5L, 6000L), // 토
            new DailyStat(LocalDate.of(2025, 4, 6), 20L, 1L, 7000L)  // 일
        );

        Long expectedDistanceSum = savedData.stream().map(DailyStat::getDistanceInMeters)
            .reduce(0L, Long::sum);
        Long expectedRunningCount = savedData.stream().map(DailyStat::getRunningCount)
            .reduce(0L, Long::sum);

        when(recordFinder.findDailyStatByUserIdBetween(eq(1L), any(LocalDateTime.class),
            any(LocalDateTime.class)))
            .thenReturn(savedData);

        // when
        WeeklyRecordStatResponse response = queryUsecase.getUserWeeklyRecordStat(
            new WeeklyStatQuery(
                1L,
                LocalDate.of(2025, 3, 31),
                LocalDate.of(2025, 4, 6)
            ));

        // then
        assertEquals(expectedDistanceSum, response.simpleStat().getTotalDistanceInMeters());
        assertEquals(expectedRunningCount, response.simpleStat().getTotalRunningCount());
    }

    @Test
    void 월간_일별_달리기_거리_합() {
        // given
        MonthlyStatQuery query = new MonthlyStatQuery(1L, 2025, 4);

        List<DailyStat> savedData = List.of(
            new DailyStat(LocalDate.of(2025, 4, 1), 20L, 1L, 2000L), // 화
            new DailyStat(LocalDate.of(2025, 4, 2), 20L, 1L, 3000L), // 수
            new DailyStat(LocalDate.of(2025, 4, 3), 20L, 1L, 4000L), // 목
            new DailyStat(LocalDate.of(2025, 4, 4), 20L, 1L, 5000L), // 금
            new DailyStat(LocalDate.of(2025, 4, 5), 20L, 1L, 6000L), // 토
            new DailyStat(LocalDate.of(2025, 4, 6), 20L, 1L, 7000L)  // 일
        );

        when(recordFinder.findDailyStatByUserIdBetween(eq(1L), any(LocalDateTime.class),
            any(LocalDateTime.class)))
            .thenReturn(savedData);

        // when
        MonthlyRecordStatResponse res = queryUsecase.getUserMonthlyRecordStat(query);

        // then
        assertEquals(savedData.size(), res.dailyStats().size());
        assertEquals(2000L, res.dailyStats().getFirst().getDistanceInMeters());
    }
}
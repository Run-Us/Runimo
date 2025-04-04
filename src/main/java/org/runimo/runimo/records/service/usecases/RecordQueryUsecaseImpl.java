package org.runimo.runimo.records.service.usecases;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.service.RecordFinder;
import org.runimo.runimo.records.service.dtos.DailyStat;
import org.runimo.runimo.records.service.usecases.dtos.MonthlyRecordStatResponse;
import org.runimo.runimo.records.service.usecases.dtos.MonthlyStatQuery;
import org.runimo.runimo.records.service.usecases.dtos.RecordDetailViewResponse;
import org.runimo.runimo.records.service.dtos.WeeklyRecordStatResponse;
import org.runimo.runimo.records.service.dtos.WeeklyStatQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecordQueryUsecaseImpl implements RecordQueryUsecase {

  private final RecordFinder recordFinder;

  @Override
  public RecordDetailViewResponse getRecordDetailView(Long recordId) {
    RunningRecord runningRecord = recordFinder.findById(recordId)
        .orElseThrow(NoSuchElementException::new);
    return RecordDetailViewResponse.from(runningRecord);
  }

  @Override
  public WeeklyRecordStatResponse getUserWeeklyRecordStat(WeeklyStatQuery query) {
    // DB에서 일별로 이미 합산된 데이터 조회
    List<DailyStat> dailyDistances = recordFinder.findDailyStatByUserIdBetween(
        query.userId(),
        query.startDate().atStartOfDay(),
        query.endDate().atTime(23, 59, 59)
    );
    return new WeeklyRecordStatResponse(dailyDistances);
  }

  @Override
  public MonthlyRecordStatResponse getUserMonthlyRecordStat(MonthlyStatQuery query) {
    LocalDate from = LocalDate.of(query.year(), query.month(), 1);
    LocalDate to = from.with(TemporalAdjusters.lastDayOfMonth());
    List<DailyStat> dailyDistances = recordFinder.findDailyStatByUserIdBetween(
        query.userId(),
        from.atStartOfDay(),
        to.atTime(23, 59, 59)
    );
    return new MonthlyRecordStatResponse(dailyDistances);
  }
}

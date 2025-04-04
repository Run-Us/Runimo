package org.runimo.runimo.records.service.usecases;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.service.RecordFinder;
import org.runimo.runimo.records.service.dtos.RunningRecordDistance;
import org.runimo.runimo.records.service.usecases.dtos.RecordDetailViewResponse;
import org.runimo.runimo.records.service.dtos.DailyStat;
import org.runimo.runimo.records.service.dtos.WeeklyRecordStatResponse;
import org.runimo.runimo.records.service.dtos.WeeklyStatQuery;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

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
    List<RunningRecordDistance> dailyDistances = recordFinder.findDailyStatByUserIdBetween(
        query.userId(),
        query.startDate().atStartOfDay(),
        query.endDate().atTime(23, 59, 59)
    );

    // 결과를 DayOfWeek 맵으로 변환
    Map<DayOfWeek, DailyStat> dailyStatsMap = dailyDistances.stream()
        .collect(Collectors.toMap(
            RunningRecordDistance::getDayOfWeek,
            RunningRecordDistance::toDailyStat
        ));

    // 월요일부터 일요일까지 순서대로 일간 데이터 정리
    List<DailyStat> weeklyStats = Arrays.stream(DayOfWeek.values())
        .map(day -> dailyStatsMap.getOrDefault(day, new DailyStat(
            query.startDate().with(day),
            0L
        )))
        .toList();

    return new WeeklyRecordStatResponse(weeklyStats);
  }
}

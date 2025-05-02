package org.runimo.runimo.records.service.usecases;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.service.RecordFinder;
import org.runimo.runimo.records.service.dto.DailyStat;
import org.runimo.runimo.records.service.dto.RecordSimpleView;
import org.runimo.runimo.records.service.dto.RecordSimpleViewResponse;
import org.runimo.runimo.records.service.dto.SimpleStat;
import org.runimo.runimo.records.service.dto.WeeklyRecordStatResponse;
import org.runimo.runimo.records.service.dto.WeeklyStatQuery;
import org.runimo.runimo.records.service.usecases.dtos.MonthlyRecordStatResponse;
import org.runimo.runimo.records.service.usecases.dtos.MonthlyStatQuery;
import org.runimo.runimo.records.service.usecases.dtos.RecordDetailViewResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordQueryUsecaseImpl implements RecordQueryUsecase {

    private final RecordFinder recordFinder;

    @Override
    public RecordDetailViewResponse getRecordDetailView(String recordId) {
        RunningRecord runningRecord = recordFinder.findByPublicId(recordId)
            .orElseThrow(NoSuchElementException::new);
        return RecordDetailViewResponse.from(runningRecord);
    }

    @Override
    public WeeklyRecordStatResponse getUserWeeklyRecordStat(WeeklyStatQuery query) {
        List<DailyStat> dailyDistances = recordFinder.findDailyStatByUserIdBetween(
            query.userId(),
            query.startDate().atStartOfDay(),
            query.endDate().atTime(23, 59, 59)
        );

        SimpleStat weeklyStat = SimpleStat.from(dailyDistances);

        return new WeeklyRecordStatResponse(weeklyStat, dailyDistances);
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
        SimpleStat monthlyStat = SimpleStat.from(dailyDistances);
        return new MonthlyRecordStatResponse(monthlyStat, dailyDistances);
    }

    @Override
    public RecordSimpleViewResponse getUserRecordSimpleView(Long id, int page, int size) {
        List<RecordSimpleView> recordSimpleViews = recordFinder.findRecordSimpleViewByUserId(id,
            page, size);
        return new RecordSimpleViewResponse(recordSimpleViews);
    }
}

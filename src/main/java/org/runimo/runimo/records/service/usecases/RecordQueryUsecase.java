package org.runimo.runimo.records.service.usecases;

import org.runimo.runimo.records.service.dtos.WeeklyRecordStatResponse;
import org.runimo.runimo.records.service.dtos.WeeklyStatQuery;
import org.runimo.runimo.records.service.usecases.dtos.MonthlyRecordStatResponse;
import org.runimo.runimo.records.service.usecases.dtos.MonthlyStatQuery;
import org.runimo.runimo.records.service.usecases.dtos.RecordDetailViewResponse;

public interface RecordQueryUsecase {

    RecordDetailViewResponse getRecordDetailView(Long publicId);

    WeeklyRecordStatResponse getUserWeeklyRecordStat(WeeklyStatQuery query);

    MonthlyRecordStatResponse getUserMonthlyRecordStat(MonthlyStatQuery query);
}

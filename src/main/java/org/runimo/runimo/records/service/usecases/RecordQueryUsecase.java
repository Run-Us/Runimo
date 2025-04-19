package org.runimo.runimo.records.service.usecases;

import org.runimo.runimo.records.service.dto.RecordSimpleViewResponse;
import org.runimo.runimo.records.service.dto.WeeklyRecordStatResponse;
import org.runimo.runimo.records.service.dto.WeeklyStatQuery;
import org.runimo.runimo.records.service.usecases.dtos.MonthlyRecordStatResponse;
import org.runimo.runimo.records.service.usecases.dtos.MonthlyStatQuery;
import org.runimo.runimo.records.service.usecases.dtos.RecordDetailViewResponse;

public interface RecordQueryUsecase {

    RecordDetailViewResponse getRecordDetailView(Long publicId);

    WeeklyRecordStatResponse getUserWeeklyRecordStat(WeeklyStatQuery query);

    MonthlyRecordStatResponse getUserMonthlyRecordStat(MonthlyStatQuery query);

    RecordSimpleViewResponse getUserRecordSimpleView(Long id, int page, int size);
}

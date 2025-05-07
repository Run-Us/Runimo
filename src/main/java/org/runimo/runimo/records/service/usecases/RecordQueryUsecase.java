package org.runimo.runimo.records.service.usecases;

import org.runimo.runimo.common.response.PageData;
import org.runimo.runimo.records.service.dto.RecordQuery;
import org.runimo.runimo.records.service.dto.RecordSimpleView;
import org.runimo.runimo.records.service.dto.WeeklyRecordStatResponse;
import org.runimo.runimo.records.service.dto.WeeklyStatQuery;
import org.runimo.runimo.records.service.usecases.dtos.MonthlyRecordStatResponse;
import org.runimo.runimo.records.service.usecases.dtos.MonthlyStatQuery;
import org.runimo.runimo.records.service.usecases.dtos.RecordDetailViewResponse;

public interface RecordQueryUsecase {

  RecordDetailViewResponse getRecordDetailView(String publicId);

  WeeklyRecordStatResponse getUserWeeklyRecordStat(WeeklyStatQuery query);

  MonthlyRecordStatResponse getUserMonthlyRecordStat(MonthlyStatQuery query);

  PageData<RecordSimpleView> getUserRecordSimpleViewByMonth(RecordQuery query);
}

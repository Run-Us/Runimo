package org.runimo.runimo.records.service.usecases.dtos;

import java.util.List;
import org.runimo.runimo.records.service.dto.DailyStat;

public record MonthlyRecordStatResponse(
    List<DailyStat> dailyStats
) {

}

package org.runimo.runimo.records.service.usecases.dtos;

import java.util.List;
import org.runimo.runimo.records.service.dtos.DailyStat;

public record MonthlyRecordStatResponse(
    List<DailyStat> dailyStats
) {

}

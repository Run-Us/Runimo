package org.runimo.runimo.records.service.usecases.dtos;

import java.util.List;
import org.runimo.runimo.records.service.dto.DailyStat;
import org.runimo.runimo.records.service.dto.SimpleStat;

public record MonthlyRecordStatResponse(
    SimpleStat simpleStat,
    List<DailyStat> dailyStats
) {

}

package org.runimo.runimo.records.service.usecases.dtos;

import org.runimo.runimo.records.service.dtos.DailyStat;

import java.util.List;

public record MonthlyRecordStatResponse(
    List<DailyStat> dailyStats
) {
}

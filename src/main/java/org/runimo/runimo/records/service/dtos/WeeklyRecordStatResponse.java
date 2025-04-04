package org.runimo.runimo.records.service.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "주간 통계 응답")
public record WeeklyRecordStatResponse(
    List<DailyStat> dailyStats
) {
}


package org.runimo.runimo.records.service.dtos;

import java.time.LocalDate;

public record WeeklyStatQuery(Long userId, LocalDate startDate, LocalDate endDate) {
}

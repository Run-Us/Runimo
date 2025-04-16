package org.runimo.runimo.records.service.dto;

import java.time.LocalDate;

public record WeeklyStatQuery(Long userId, LocalDate startDate, LocalDate endDate) {

}

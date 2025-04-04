package org.runimo.runimo.records.service.dtos;


import java.time.LocalDate;

public record DailyStat(
    LocalDate date,
    Long distance
) {
}


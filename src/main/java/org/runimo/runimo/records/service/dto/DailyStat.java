package org.runimo.runimo.records.service.dto;


import java.time.LocalDate;

public record DailyStat(
    LocalDate date,
    Long distance
) {

}


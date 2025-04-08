package org.runimo.runimo.records.service.usecases.dtos;

public record MonthlyStatQuery(
    Long userId,
    Integer year,
    Integer month
) {

}

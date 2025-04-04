package org.runimo.runimo.records.service.dtos;

import java.time.DayOfWeek;
import java.time.LocalDate;

public record RunningRecordDistance(
    Long totalDistanceInMeters,
    LocalDate date
) {
  public DayOfWeek getDayOfWeek() {
    return date.getDayOfWeek();
  }
  public DailyStat toDailyStat() {
    return new DailyStat(date, totalDistanceInMeters);
  }
}

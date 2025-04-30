package org.runimo.runimo.records.service.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.Getter;

@Getter
public class DailyStat {

    private LocalDate date;
    @JsonIgnore
    private Long timeInSeconds;
    @JsonIgnore
    private Long runningCount;
    private Long distanceInMeters;

    public DailyStat(LocalDate date, Long timeInSeconds, Long runningCount, Long distanceInMeters) {
        this.date = date;
        this.timeInSeconds = timeInSeconds;
        this.runningCount = runningCount;
        this.distanceInMeters = distanceInMeters;
    }

    public void addData(RecordStatDto recordStat) {
        this.timeInSeconds += recordStat.getStartedAt().until(recordStat.getEndAt(), ChronoUnit.SECONDS);
        this.runningCount += 1;
        this.distanceInMeters += recordStat.getRunningDistanceInMeters();
    }

    public static DailyStat empty(LocalDate date) {
        return new DailyStat(date, 0L, 0L, 0L);
    }
}


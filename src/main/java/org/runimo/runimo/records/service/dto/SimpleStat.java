package org.runimo.runimo.records.service.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimpleStat {

    Long totalTimeInSeconds;
    Long totalRunningCount;
    Long totalDistanceInMeters;

    private SimpleStat(Long totalTimeInSeconds, Long totalRunningCount, Long totalDistanceInMeters) {
        this.totalTimeInSeconds = totalTimeInSeconds;
        this.totalRunningCount = totalRunningCount;
        this.totalDistanceInMeters = totalDistanceInMeters;
    }



    public static SimpleStat from(List<DailyStat> dailyStats) {
        SimpleStat simpleStat = new SimpleStat(0L, 0L, 0L);
        for (DailyStat dailyStat : dailyStats) {
            simpleStat.totalTimeInSeconds += dailyStat.getTimeInSeconds();
            simpleStat.totalRunningCount += dailyStat.getRunningCount();
            simpleStat.totalDistanceInMeters += dailyStat.getDistanceInMeters();
        }
        return simpleStat;
    }
}

package org.runimo.runimo.runimo.service.dtos;

import lombok.Getter;

@Getter
public class MainRunimoStat {

    private final String name;
    private final String imageUrl;
    private final Long totalRunningCount;
    private final Long totalDistanceInMeters;

    public MainRunimoStat(String name, String imageUrl, Long totalRunningCount,
        Long totalDistanceInMeters) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.totalRunningCount = totalRunningCount;
        this.totalDistanceInMeters = totalDistanceInMeters;
    }
}

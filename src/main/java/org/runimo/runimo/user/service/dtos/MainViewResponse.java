package org.runimo.runimo.user.service.dtos;

public record MainViewResponse(
    String nickname,
    String profileImageUrl,
    Long lovePoint,
    Long totalDistanceInMeters,
    Long totalRunningCount,
    Long totalEggCount
) {
}

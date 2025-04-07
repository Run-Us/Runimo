package org.runimo.runimo.user.service.dtos;

public record MyPageViewResponse(
    String nickname,
    String profileImageUrl,
    Long totalDistanceInMeters,
    Long latestRunDateBefore,
    LatestRunningRecord latestRunningRecordNullable
) {
}

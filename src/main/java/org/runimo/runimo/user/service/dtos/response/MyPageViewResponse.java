package org.runimo.runimo.user.service.dtos.response;

import org.runimo.runimo.user.service.dtos.LatestRunningRecord;

public record MyPageViewResponse(
    String nickname,
    String profileImageUrl,
    Long totalDistanceInMeters,
    Long latestRunDateBefore,
    LatestRunningRecord latestRunningRecordNullable
) {

}

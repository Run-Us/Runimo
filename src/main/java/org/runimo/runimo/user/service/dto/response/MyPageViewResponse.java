package org.runimo.runimo.user.service.dto.response;

import org.runimo.runimo.user.service.dto.LatestRunningRecord;

public record MyPageViewResponse(
    String nickname,
    String profileImageUrl,
    Long totalDistanceInMeters,
    Long latestRunDateBefore,
    LatestRunningRecord latestRunningRecordNullable
) {

}

package org.runimo.runimo.rewards.controller.requests;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리워드 클레임 요청 DTO")
public record RewardClaimRequest(
    @Schema(description = "보상을 요청하는 달리기 기록 ID", example = "1") Long recordId
) {

}

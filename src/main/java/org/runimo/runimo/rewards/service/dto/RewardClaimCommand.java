package org.runimo.runimo.rewards.service.dto;

public record RewardClaimCommand(
    Long userId,
    String recordPublicId
) {

}

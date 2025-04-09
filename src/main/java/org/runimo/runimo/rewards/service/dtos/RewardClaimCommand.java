package org.runimo.runimo.rewards.service.dtos;

public record RewardClaimCommand(
    Long userId,
    Long recordId
) {

}

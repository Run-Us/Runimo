package org.runimo.runimo.rewards.service.dto;


public record RewardResponse(
    Boolean isRewarded,
    String eggCode,
    String eggType,
    Long lovePointAmount
) {

}

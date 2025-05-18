package org.runimo.runimo.rewards.service.dto;


import org.runimo.runimo.item.domain.Egg;

public record RewardResponse(
    Boolean isRewarded,
    String eggCode,
    String eggType,
    String eggImgUrl,
    Long lovePointAmount
) {

    public static RewardResponse of(Egg egg, Long lovePointAmount) {
        return new RewardResponse(
            egg != Egg.EMPTY,
            egg.getItemCode(),
            egg.getName(),
            egg.getImgUrl(),
            lovePointAmount
        );
    }

}

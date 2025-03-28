package org.runimo.runimo.rewards.service.dtos;

import org.runimo.runimo.item.domain.EggType;

public record RewardResponse(
    String eggCode,
    EggType eggType,
    Long lovePointAmount
) {
}

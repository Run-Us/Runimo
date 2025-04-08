package org.runimo.runimo.item.service.dtos;

import org.runimo.runimo.item.domain.ActivityType;

public record CreateActivityCommand(
    Long itemId,
    Long userId,
    Long quantity,
    ActivityType activityType
) {

}

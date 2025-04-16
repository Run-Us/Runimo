package org.runimo.runimo.item.service.dto;

import org.runimo.runimo.item.domain.ActivityType;

public record CreateActivityCommand(
    Long itemId,
    Long userId,
    Long quantity,
    ActivityType activityType
) {

}

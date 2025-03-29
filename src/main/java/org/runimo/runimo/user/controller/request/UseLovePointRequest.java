package org.runimo.runimo.user.controller.request;

public record UseLovePointRequest(
    Long itemId,
    Long lovePointAmount
) {
}

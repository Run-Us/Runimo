package org.runimo.runimo.user.controller;

public record UseLovePointRequest(
    Long itemId,
    Long lovePointAmount
) {
}

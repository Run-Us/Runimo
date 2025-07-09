package org.runimo.runimo.user.service.dto.command;

public record FeedbackCommand(
    Long userId,
    Integer rate,
    String content
) {

}

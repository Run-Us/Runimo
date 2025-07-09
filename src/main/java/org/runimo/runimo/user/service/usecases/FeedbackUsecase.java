package org.runimo.runimo.user.service.usecases;

import org.runimo.runimo.user.service.dto.command.FeedbackCommand;

public interface FeedbackUsecase {

    void createFeedback(FeedbackCommand command);
}

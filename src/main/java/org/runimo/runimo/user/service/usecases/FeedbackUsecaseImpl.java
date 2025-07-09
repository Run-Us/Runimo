package org.runimo.runimo.user.service.usecases;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.user.domain.Feedback;
import org.runimo.runimo.user.repository.FeedbackRepository;
import org.runimo.runimo.user.service.dto.command.FeedbackCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackUsecaseImpl implements FeedbackUsecase {

    private final FeedbackRepository feedbackRepository;

    @Override
    @Transactional
    public void createFeedback(FeedbackCommand command) {
        Feedback feedback = Feedback.builder()
            .userId(command.userId())
            .rate(command.rate())
            .content(command.content())
            .build();
        feedbackRepository.save(feedback);
    }
}

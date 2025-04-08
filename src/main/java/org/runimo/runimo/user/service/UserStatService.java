package org.runimo.runimo.user.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.service.usecases.dtos.RecordCreateCommand;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserStatService {

    private final UserRepository userStatRepository;

    @Transactional
    public void updateUserStats(User user, RecordCreateCommand command) {
        user.updateRunningStats(
            command.totalDistanceInMeters(),
            command.totalDurationInSeconds()
        );
        userStatRepository.save(user);
    }

}

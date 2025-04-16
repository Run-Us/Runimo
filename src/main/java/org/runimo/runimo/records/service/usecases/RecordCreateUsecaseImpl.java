package org.runimo.runimo.records.service.usecases;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.service.RecordCommandService;
import org.runimo.runimo.records.service.usecases.dtos.RecordCreateCommand;
import org.runimo.runimo.records.service.usecases.dtos.RecordSaveResponse;
import org.runimo.runimo.runimo.service.RunimoService;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.UserStatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecordCreateUsecaseImpl implements RecordCreateUsecase {

    private final UserFinder userFinder;
    private final UserStatService userStatService;
    private final RecordCommandService commandService;
    private final RunimoService runimoService;

    @Override
    @Transactional
    public RecordSaveResponse execute(RecordCreateCommand command) {
        User user = userFinder.findUserById(command.userId())
            .orElseThrow(NoSuchElementException::new);
        userStatService.updateUserStats(user, command);
        runimoService.updateRunimoStat(user.getMainRunimoId(), command.totalDistanceInMeters());
        return commandService.saveRecord(user.getId(), command);
    }
}

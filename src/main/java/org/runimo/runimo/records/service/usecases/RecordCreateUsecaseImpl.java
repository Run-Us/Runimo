package org.runimo.runimo.records.service.usecases;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.service.RecordCommandService;
import org.runimo.runimo.records.service.usecases.model.RecordCreateCommand;
import org.runimo.runimo.records.service.usecases.model.RecordSaveResponse;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserFinder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RecordCreateUsecaseImpl implements RecordCreateUsecase {

  private final UserFinder userFinder;
  private final RecordCommandService commandService;

  @Override
  public RecordSaveResponse execute(RecordCreateCommand command) {
    User user = userFinder.findUserByPublicId(command.userPublicId())
        .orElseThrow(NoSuchElementException::new);
    commandService.saveRecord(user.getId(), command);
    return null;
  }
}

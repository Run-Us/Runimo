package org.runimo.runimo.records.service.usecases;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.service.RecordCommandService;
import org.runimo.runimo.records.service.usecases.dtos.RecordUpdateCommand;
import org.runimo.runimo.user.service.UserFinder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RecordUpdateUsecaseImpl implements RecordUpdateUsecase {

  private final RecordCommandService recordCommandService;
  private final UserFinder userFinder;

  @Override
  public void updateRecord(RecordUpdateCommand command) {
    recordCommandService.updateRecord(command);
  }
}

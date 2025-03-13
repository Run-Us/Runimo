package org.runimo.runimo.records.service.usecases;

import org.runimo.runimo.records.service.usecases.model.RecordUpdateCommand;

public interface RecordUpdateUsecase {
  void updateRecord(RecordUpdateCommand command);
}

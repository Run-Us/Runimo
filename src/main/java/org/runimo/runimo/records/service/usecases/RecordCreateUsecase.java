package org.runimo.runimo.records.service.usecases;

import org.runimo.runimo.records.service.usecases.model.RecordCreateCommand;
import org.runimo.runimo.records.service.usecases.model.RecordSaveResponse;

public interface RecordCreateUsecase {
  RecordSaveResponse execute(RecordCreateCommand command);
}

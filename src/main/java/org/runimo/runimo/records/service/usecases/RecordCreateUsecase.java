package org.runimo.runimo.records.service.usecases;

import org.runimo.runimo.records.service.usecases.dtos.RecordCreateCommand;
import org.runimo.runimo.records.service.usecases.dtos.RecordSaveResponse;

public interface RecordCreateUsecase {
  RecordSaveResponse execute(RecordCreateCommand command);
}

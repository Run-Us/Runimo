package org.runimo.runimo.records.service.usecases;

import org.runimo.runimo.records.service.usecases.model.RecordDetailViewResponse;

public interface RecordQueryUsecase {
  RecordDetailViewResponse getRecordDetailView(String publicId);
}

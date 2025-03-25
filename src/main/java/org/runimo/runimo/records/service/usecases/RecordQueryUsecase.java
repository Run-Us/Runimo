package org.runimo.runimo.records.service.usecases;

import org.runimo.runimo.records.service.usecases.dtos.RecordDetailViewResponse;

public interface RecordQueryUsecase {
  RecordDetailViewResponse getRecordDetailView(String publicId);
}

package org.runimo.runimo.records.service.usecases;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.service.RecordFinder;
import org.runimo.runimo.records.service.usecases.dtos.RecordDetailViewResponse;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RecordQueryUsecaseImpl implements RecordQueryUsecase {

  private final RecordFinder recordFinder;

  @Override
  public RecordDetailViewResponse getRecordDetailView(Long recordId) {
    RunningRecord runningRecord = recordFinder.findById(recordId)
        .orElseThrow(NoSuchElementException::new);
    return RecordDetailViewResponse.from(runningRecord);
  }
}

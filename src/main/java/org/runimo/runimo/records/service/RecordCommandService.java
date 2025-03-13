package org.runimo.runimo.records.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.common.scale.Pace;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.repository.RecordRepository;
import org.runimo.runimo.records.service.usecases.model.RecordCreateCommand;
import org.runimo.runimo.records.service.usecases.model.RecordSaveResponse;
import org.runimo.runimo.records.service.usecases.model.RecordUpdateCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class RecordCommandService {

  private final RecordRepository recordRepository;

  @Transactional
  public RecordSaveResponse saveRecord(Long userId, RecordCreateCommand command) {
    RunningRecord runningRecord = mapToRunningRecord(userId, command);
    recordRepository.save(runningRecord);
    return new RecordSaveResponse(runningRecord.getId());
  }

  @Transactional
  public void updateRecord(RecordUpdateCommand command) {
    RunningRecord runningRecord = recordRepository.findByRecordPublicId(command.recordPublicId())
        .orElseThrow(NoSuchElementException::new);
    runningRecord.update(mapToUpdateRecord(command));
    recordRepository.save(runningRecord);
  }

  private RunningRecord mapToUpdateRecord(RecordUpdateCommand command) {
    return RunningRecord.withoutId(
        command.editorId(),
        command.title(),
        command.startedAt(),
        command.endAt(),
        new Distance(command.totalDistanceInMeters()),
        new Pace(command.averagePaceInMilliSeconds())
    );
  }

  private RunningRecord mapToRunningRecord(Long id, RecordCreateCommand command) {
    return RunningRecord.builder()
        .userId(id)
        .startedAt(command.startedAt())
        .endAt(command.endAt())
        .averagePace(command.averagePace())
        .totalDistance(command.totalDistance())
        .build();
  }
}

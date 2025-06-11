package org.runimo.runimo.records.service;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.repository.RecordRepository;
import org.runimo.runimo.records.service.usecases.dtos.RecordCreateCommand;
import org.runimo.runimo.records.service.usecases.dtos.RecordUpdateCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RecordCommandService {

    private final RecordRepository recordRepository;

    @Transactional
    public RunningRecord saveRecord(Long userId, RecordCreateCommand command) {
        RunningRecord runningRecord = mapToCreatedRunningRecord(userId, command);
        return recordRepository.save(runningRecord);
    }

    @Transactional
    public void updateRecord(RecordUpdateCommand command) {
        RunningRecord runningRecord = recordRepository.findByRecordPublicId(
                command.recordPublicId())
            .orElseThrow(NoSuchElementException::new);

        runningRecord.updateTitle(command.editorId(), command.title());
        runningRecord.updateDescription(command.editorId(), command.description());
        runningRecord.updateImageUrl(command.editorId(), command.imgUrl());
        recordRepository.save(runningRecord);
    }


    private RunningRecord mapToCreatedRunningRecord(Long id, RecordCreateCommand command) {
        return RunningRecord.builder()
            .userId(id)
            .startedAt(command.startedAt())
            .endAt(command.endAt())
            .averagePace(command.averagePace())
            .totalDistance(command.totalDistance())
            .totalTimeInSeconds(command.totalTimeInSeconds())
            .isRewarded(false)
            .pacePerKm(command.segmentPaces())
            .build();
    }
}

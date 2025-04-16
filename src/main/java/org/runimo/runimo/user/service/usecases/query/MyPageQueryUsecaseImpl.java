package org.runimo.runimo.user.service.usecases.query;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.service.RecordFinder;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.dto.LatestRunningRecord;
import org.runimo.runimo.user.service.dto.response.MyPageViewResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageQueryUsecaseImpl implements MyPageQueryUsecase {

    private final UserFinder userFinder;
    private final RecordFinder recordFinder;

    @Override
    public MyPageViewResponse execute(Long userId) {
        User user = userFinder.findUserById(userId)
            .orElseThrow(NoSuchElementException::new);
        Optional<RunningRecord> optionalRecord = recordFinder.findLatestRunningRecordByUserId(
            userId);
        Long daysSinceLastRun = optionalRecord
            .map(runningRecord -> ChronoUnit.DAYS.between(runningRecord.getStartedAt(),
                LocalDateTime.now()))
            .orElse(0L);
        LatestRunningRecord latestRunningRecord = optionalRecord
            .map(this::toLatestRunningRecord)
            .orElse(null);
        return new MyPageViewResponse(
            user.getNickname(),
            user.getImgUrl(),
            user.getTotalDistanceInMeters(),
            daysSinceLastRun,
            latestRunningRecord
        );
    }

    private LatestRunningRecord toLatestRunningRecord(RunningRecord runningRecord) {
        return new LatestRunningRecord(
            runningRecord.getTitle(),
            runningRecord.getStartedAt(),
            runningRecord.getTotalDistance().getAmount(),
            runningRecord.getRunningTime().getSeconds(),
            runningRecord.getAveragePace().getPaceInMilliSeconds()
        );
    }
}

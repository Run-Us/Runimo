package org.runimo.runimo.user.service.usecases.query;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.service.RecordFinder;
import org.runimo.runimo.user.service.dtos.MyPageViewResponse;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.dtos.LatestRunningRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MyPageQueryUsecaseImpl implements MyPageQueryUsecase {

  private final UserFinder userFinder;
  private final RecordFinder recordFinder;

  @Override
  public MyPageViewResponse execute(Long userId) {

    User user = userFinder.findUserById(userId)
        .orElseThrow(NoSuchElementException::new);
    RunningRecord runningRecord = recordFinder.findLatestRunningRecordByUserId(userId)
        .orElseThrow(NoSuchElementException::new);
    Long differenceBetweenTodayAndLastRunningDate =
        ChronoUnit.DAYS.between(runningRecord.getStartedAt(), LocalDateTime.now());
    return new MyPageViewResponse(
        user.getNickname(),
        user.getImgUrl(),
        user.getTotalDistanceInMeters(),
        differenceBetweenTodayAndLastRunningDate,
        new LatestRunningRecord(
            runningRecord.getTitle(),
            runningRecord.getStartedAt(),
            runningRecord.getTotalDistance().getAmount(),
            runningRecord.getRunningTime().getSeconds(),
            runningRecord.getAveragePace().getPaceInMilliSeconds()
        )
    );
  }
}

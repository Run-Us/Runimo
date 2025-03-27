package org.runimo.runimo.rewards.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.records.service.RecordFinder;
import org.runimo.runimo.rewards.service.eggs.EggGrantService;
import org.runimo.runimo.rewards.service.dtos.RewardClaimCommand;
import org.runimo.runimo.rewards.service.dtos.RewardResponse;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardService {

  private final RecordFinder recordFinder;
  private final UserFinder userFinder;
  private final EggGrantService eggGrantService;

  @Transactional
  public RewardResponse claimReward(RewardClaimCommand command) {
    RunningRecord runningRecord = recordFinder.findById(command.recordId())
        .orElseThrow(NoSuchElementException::new);
    validateRecord(runningRecord);
    Egg grantedEgg = rewardEgg(command);
    runningRecord.reward(command.userId());
    //TODO: 애정 보상
    return new RewardResponse(grantedEgg.getItemCode(), grantedEgg.getEggType());
  }

  private Egg rewardEgg(RewardClaimCommand command) {
     User user = userFinder.findUserById(command.userId())
        .orElseThrow(NoSuchElementException::new);
     if(validateRecordIsFirstRecordOfWeek(command)) {
        return eggGrantService.grantRandomEggToUser(user);
     }
     return Egg.EMPTY;
  }

  private void validateRecord(RunningRecord runningRecord) {
    if(runningRecord.isRecordAlreadyRewarded()) throw new IllegalStateException("이미 보상이 지급되었습니다.");
  }

  private boolean validateRecordIsFirstRecordOfWeek(RewardClaimCommand command) {
    Optional<RunningRecord> firstRecordOfWeek = recordFinder.findFirstRunOfCurrentWeek(command.userId());
    if(firstRecordOfWeek.isEmpty()) {
      log.info("유저 {}의 첫번째 달리기 기록이 없습니다.", command.userId());
      return false;
    }
    if(!command.recordId().equals(firstRecordOfWeek.get().getId())){
      log.info("유저 {}의 첫번째 달리기 기록이 아닙니다.", command.userId());
      return false;
    }
    return true;
  }
}

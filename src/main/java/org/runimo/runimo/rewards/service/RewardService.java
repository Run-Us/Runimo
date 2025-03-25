package org.runimo.runimo.rewards.service;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.records.service.RecordFinder;
import org.runimo.runimo.rewards.service.eggs.EggGrantService;
import org.runimo.runimo.rewards.service.dtos.RewardClaimCommand;
import org.runimo.runimo.rewards.service.dtos.RewardResponse;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserFinder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RewardService {

  private final RecordFinder recordFinder;
  private final UserFinder userFinder;
  private final EggGrantService eggGrantService;

  public RewardResponse claimReward(RewardClaimCommand command) {
    User user = userFinder.findUserById(command.userId())
        .orElseThrow(NoSuchElementException::new);
    recordFinder.findById(command.recordId())
        .orElseThrow(NoSuchElementException::new);
    // grant egg
    Egg grantedEgg = eggGrantService.grantRandomEggToUser(user);
    //TODO: 애정 보상
    return new RewardResponse(grantedEgg.getItemCode(), grantedEgg.getEggType());
  }
}

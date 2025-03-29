package org.runimo.runimo.user.service.usecases.query;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.records.service.RecordFinder;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.UserItemFinder;
import org.runimo.runimo.user.service.dtos.MainViewResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainViewQueryUsecaseImpl implements MainViewQueryUsecase {

  private final UserFinder userFinder;
  private final UserItemFinder userItemFinder;
  private final RecordFinder recordFinder;

  @Override
  public MainViewResponse execute(Long userId) {
    // 유저 달리기 스탯
    User user = userFinder.findUserById(userId)
        .orElseThrow(NoSuchElementException::new);
    List<UserItem> userEggs = userItemFinder.findEggsByUserId(userId);
    Long eggCount = userEggs.stream()
        .map(UserItem::getQuantity)
        .collect(Collectors.summarizingLong(Long::longValue))
        .getSum();
    Long runningCount = recordFinder.countByUserId(userId);
    Long lovePoint = userFinder.findLovePointByUserId(userId)
        .orElseThrow(NoSuchElementException::new)
        .getAmount();
    return new MainViewResponse(
        user.getNickname(),
        user.getImgUrl(),
        lovePoint,
        user.getTotalDistanceInMeters(),
        runningCount,
        eggCount
    );
  }
}

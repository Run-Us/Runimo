package org.runimo.runimo.user.service.usecases.query;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.runimo.service.model.MainRunimoStat;
import org.runimo.runimo.runimo.service.usecase.RunimoFinder;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.UserItemFinder;
import org.runimo.runimo.user.service.dtos.MainViewResponse;
import org.runimo.runimo.user.service.dtos.UserMainViewInfo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainViewQueryUsecaseImpl implements MainViewQueryUsecase {

    private final UserFinder userFinder;
    private final UserItemFinder userItemFinder;
    private final RunimoFinder runimoFinder;

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
        MainRunimoStat mainRunimoStat = runimoFinder.findMainRunimoStatByUserId(user.getMainRunimoId())
            .orElse(null);
        Long lovePoint = userFinder.findLovePointByUserId(userId)
            .orElseThrow(NoSuchElementException::new)
            .getAmount();
        return new MainViewResponse(
            mainRunimoStat,
            new UserMainViewInfo(
                lovePoint,
                eggCount
            )
        );
    }
}

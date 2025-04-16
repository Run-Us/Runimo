package org.runimo.runimo.user.service.usecases.query;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.runimo.runimo.runimo.service.dtos.MainRunimoStat;
import org.runimo.runimo.runimo.service.usecase.RunimoFinder;
import org.runimo.runimo.user.domain.LovePoint;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.domain.UserItem;
import org.runimo.runimo.user.service.UserFinder;
import org.runimo.runimo.user.service.UserItemFinder;
import org.runimo.runimo.user.service.dtos.response.MainViewResponse;
import org.runimo.runimo.user.service.dtos.UserMainViewInfo;
import org.springframework.stereotype.Service;

@Slf4j
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
        MainRunimoStat mainRunimoStat = findMainRunimoStatIfRegistered(user.getMainRunimoId());
        UserMainViewInfo userMainViewInfo = buildUserMainViewInfo(user.getId());
        return new MainViewResponse(
            mainRunimoStat,
            userMainViewInfo
        );
    }

    private MainRunimoStat findMainRunimoStatIfRegistered(Long mainRunimoId) {
        if(mainRunimoId == null) {
            return null;
        }
        return runimoFinder.findMainRunimoStatByUserId(mainRunimoId)
            .orElseThrow(NoSuchElementException::new);
    }

    private UserMainViewInfo buildUserMainViewInfo(Long userId) {
        long eggCount = calculateUserEggCount(userId);
        long lovePoint = findUserLovePointOrThrow(userId);
        return new UserMainViewInfo(lovePoint, eggCount);
    }

    private long calculateUserEggCount(Long userId) {
        List<UserItem> userEggs = userItemFinder.findEggsByUserId(userId);
        return userEggs.stream()
            .mapToLong(UserItem::getQuantity)
            .sum();
    }

    private long findUserLovePointOrThrow(Long userId) {
        return userFinder.findLovePointByUserId(userId)
            .map(LovePoint::getAmount)
            .orElseThrow(() -> {
                log.error("[ERR] id :{} love_point_not_matched", userId);
                return new NoSuchElementException();
            });
    }
}

package org.runimo.runimo.rewards.service.lovepoint;

import static org.runimo.runimo.common.GlobalConsts.DISTANCE_UNIT;

import lombok.RequiredArgsConstructor;
import org.runimo.runimo.common.scale.Distance;
import org.runimo.runimo.records.domain.RunningRecord;
import org.runimo.runimo.user.domain.LovePoint;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.LovePointProcessor;
import org.runimo.runimo.user.service.UserFinder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class LoveGrantService {

    private final UserFinder userFinder;
    private final LovePointProcessor lovePointProcessor;

    public Long grantLoveToUserWithDistance(RunningRecord runningRecord) {
        User user = userFinder.findUserById(runningRecord.getUserId())
            .orElseThrow(IllegalStateException::new);
        Long loveAmount = calculateLoveAmount(runningRecord.getTotalDistance());
        LovePoint lovePoint = lovePointProcessor.acquireLovePoint(user.getId(), loveAmount);
        return lovePoint.getAmount();
    }

    private Long calculateLoveAmount(Distance distance) {
        return distance.divide(DISTANCE_UNIT).getAmount();
    }
}

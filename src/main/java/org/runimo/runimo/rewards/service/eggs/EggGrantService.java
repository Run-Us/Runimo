package org.runimo.runimo.rewards.service.eggs;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.item.domain.EggType;
import org.runimo.runimo.item.repository.EggTypeRepository;
import org.runimo.runimo.item.service.EggFactory;
import org.runimo.runimo.user.domain.User;
import org.runimo.runimo.user.service.UserItemProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EggGrantService {

    // 회원가입 시 지급하는 알
    private static final Long GREETING_EGG_AMOUNT = 1L;
    private static final Long DEFAULT_REWARD_EGG_AMOUNT = 1L;
    private final EggFactory eggFactory;
    private final UserItemProcessor userItemProcessor;
    private final EggTypeRepository eggTypeRepository;

    @Transactional
    public Egg grantGreetingEggToUser(User user) {
        if (!user.checkUserFirstRun()) {
            return Egg.EMPTY;
        }
        Egg grantedEgg = eggFactory.createGreetingEgg();
        userItemProcessor.updateItemQuantity(user.getId(), grantedEgg.getId(), GREETING_EGG_AMOUNT);
        return grantedEgg;
    }

    @Transactional
    public Egg grantRandomEggToUser(User user) {
        List<EggType> unLockedEggTypes = eggTypeRepository.findEggTypeByRequiredDistanceInMetersLessThanEqual(
            user.getTotalDistanceInMeters());
        Egg grantedEgg = eggFactory.createEggRandomly(unLockedEggTypes);
        userItemProcessor.updateItemQuantity(user.getId(), grantedEgg.getId(),
            DEFAULT_REWARD_EGG_AMOUNT);
        return grantedEgg;
    }
}

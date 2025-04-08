package org.runimo.runimo.item.service;

import java.security.SecureRandom;
import java.util.List;
import org.runimo.runimo.item.domain.EggType;
import org.springframework.stereotype.Component;

@Component
public class EggTypeRandomGenerator {

    private final SecureRandom secureRandom = new SecureRandom();

    public EggType generateRandomEggType(List<EggType> unLockedEggTypes) {
        double eachEggTypeProbability = 1.0 / unLockedEggTypes.size();
        double cumulatedProbability = 0.0;
        double randomValue = secureRandom.nextDouble();
        for (EggType eggType : unLockedEggTypes) {
            cumulatedProbability += eachEggTypeProbability;
            if (randomValue < cumulatedProbability) {
                return eggType;
            }
        }
        return unLockedEggTypes.getLast();
    }
}

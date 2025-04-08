package org.runimo.runimo.item.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.runimo.runimo.item.domain.EggType;

class EggTypeRandomGeneratorTest {


    private final EggTypeRandomGenerator eggTypeRandomGenerator = new EggTypeRandomGenerator();

    @Test
    void generateRandomEggType() {
        //given
        Long totalRunningDistanceInMeters = 34444L;
        //when
        for (int i = 0; i < 10; i++) {
            EggType selectedType = eggTypeRandomGenerator.generateRandomEggType(
                EggType.getUnLockedEggTypes(totalRunningDistanceInMeters));
            assertNotNull(selectedType);
        }
    }

}
package org.runimo.runimo.item.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.runimo.runimo.item.domain.EggType;

class EggEnumRandomGeneratorTest {


    private final EggTypeRandomGenerator eggTypeRandomGenerator = new EggTypeRandomGenerator();

    @Test
    void generateRandomEggType() {
        //when
        List<EggType> eggTypes = List.of(
            EggType.of(
                "마당",
                "E100",
                0L,
                0
            ),
            EggType.of(
                "숲",
                "E101",
                30000L,
                1
            )
        );
        for (int i = 0; i < 10; i++) {
            EggType selectedType = eggTypeRandomGenerator.generateRandomEggType(eggTypes);
            assertNotNull(selectedType);
        }
    }

}
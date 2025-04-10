package org.runimo.runimo.item.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.runimo.runimo.user.domain.EggStatus;
import org.runimo.runimo.user.domain.IncubatingEgg;

class IncubatingEggTest {

    @Test
    void gainLovePoint_성공() {
        // given
        IncubatingEgg incubatingEgg = IncubatingEgg.builder()
            .userId(1L)
            .eggId(100L)
            .currentLovePointAmount(0L)
            .hatchRequireAmount(10L)
            .status(EggStatus.INCUBATING)
            .build();

        // when
        incubatingEgg.gainLovePoint(5L);

        // then
        assertEquals(5L, incubatingEgg.getCurrentLovePointAmount());
    }

    @Test
    void gainLovePoint_성공_부화조건_충족() {
        // given
        IncubatingEgg incubatingEgg = IncubatingEgg.builder()
            .userId(1L)
            .eggId(100L)
            .currentLovePointAmount(0L)
            .hatchRequireAmount(10L)
            .status(EggStatus.INCUBATING)
            .build();

        // when
        incubatingEgg.gainLovePoint(10L);

        // then
        assertEquals(10L, incubatingEgg.getCurrentLovePointAmount());
        assertEquals(EggStatus.INCUBATED, incubatingEgg.getStatus());
    }

    @Test
    void gainLovePoint_실패_이미_부화됨() {
        // given
        IncubatingEgg incubatingEgg = IncubatingEgg.builder()
            .userId(1L)
            .eggId(100L)
            .currentLovePointAmount(10L)
            .hatchRequireAmount(10L)
            .status(EggStatus.INCUBATED)
            .build();

        // when & then
        assertThrows(IllegalStateException.class, () -> incubatingEgg.gainLovePoint(1L));
    }

    @Test
    void hatch_성공() {
        // given
        IncubatingEgg incubatingEgg = IncubatingEgg.builder()
            .userId(1L)
            .eggId(100L)
            .currentLovePointAmount(10L)
            .hatchRequireAmount(10L)
            .status(EggStatus.INCUBATED)
            .build();

        // when
        incubatingEgg.hatch();

        // then
        assertEquals(EggStatus.HATCHED, incubatingEgg.getStatus());
    }

    @Test
    void hatch_실패_애정포인트_미달() {
        // given
        IncubatingEgg incubatingEgg = IncubatingEgg.builder()
            .userId(1L)
            .eggId(100L)
            .currentLovePointAmount(5L)
            .hatchRequireAmount(10L)
            .status(EggStatus.INCUBATING)
            .build();

        // when & then
        assertThrows(IllegalStateException.class, incubatingEgg::hatch);
    }

    @Test
    void gainLovePoint_과다_지급() {
        // given
        IncubatingEgg incubatingEgg = IncubatingEgg.builder()
            .userId(1L)
            .eggId(100L)
            .currentLovePointAmount(0L)
            .hatchRequireAmount(10L)
            .status(EggStatus.INCUBATING)
            .build();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            incubatingEgg.gainLovePoint(15L);
        });
    }
}

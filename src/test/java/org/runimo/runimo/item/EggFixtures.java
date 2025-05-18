package org.runimo.runimo.item;

import org.runimo.runimo.item.domain.Egg;
import org.runimo.runimo.item.domain.EggType;

public final class EggFixtures {

    public static final String TEST_EGG_CODE = "TEST_EGG";
    public static final String TEST_EGG_NAME = "Test Egg";
    public static final String TEST_EGG_DESCRIPTION = "A test egg for testing purposes";
    public static final String TEST_EGG_IMG_URL = "test_egg.png";

    public static final String TEST_EGG_TYPE_NAME = "Test Type";
    public static final String TEST_EGG_TYPE_CODE = "TEST";
    public static final Long TEST_EGG_TYPE_DISTANCE = 1000L;
    public static final Integer TEST_EGG_TYPE_LEVEL = 1;

    public static Egg createDefaultEgg() {
        return Egg.builder()
            .itemCode(TEST_EGG_CODE)
            .name(TEST_EGG_NAME)
            .description(TEST_EGG_DESCRIPTION)
            .imgUrl(TEST_EGG_IMG_URL)
            .eggType(createDefaultEggType())
            .hatchRequireAmount(10L)
            .build();
    }

    public static Egg createCustomEgg(String itemCode, String name, String description,
        String imgUrl, EggType eggType, Long hatchRequireAmount) {
        return Egg.builder()
            .itemCode(itemCode)
            .name(name)
            .description(description)
            .imgUrl(imgUrl)
            .eggType(eggType)
            .hatchRequireAmount(hatchRequireAmount)
            .build();
    }

    public static EggType createDefaultEggType() {
        return EggType.of(TEST_EGG_TYPE_NAME, TEST_EGG_TYPE_CODE, TEST_EGG_TYPE_DISTANCE,
            TEST_EGG_TYPE_LEVEL);
    }

    public static EggType createCustomEggType(String name, String code,
        Long requiredDistanceInMeters, Integer level) {
        return EggType.of(name, code, requiredDistanceInMeters, level);
    }

}
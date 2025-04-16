package org.runimo.runimo.runimo.service.dtos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.runimo.runimo.item.domain.EggType;

public record RunimoTypeSimpleModel(
    String name,
    String imgUrl,
    String code,
    String eggType,
    String description
) {

    public RunimoTypeSimpleModel(String name, String imgUrl, String code, EggType eggType,
        String description) {
        this(name, imgUrl, code, eggType.getName(), description);
    }

    public static List<RunimoTypeGroup> toDtoList(List<RunimoTypeSimpleModel> modelList) {
        Map<String, List<RunimoTypeInfo>> runimoDtoMap = toRunimoTypeMap(
            modelList);

        List<RunimoTypeGroup> runimoTypeGroups = toRunimoTypeGroups(
            runimoDtoMap);

        return runimoTypeGroups;
    }

    private RunimoTypeInfo toDto() {
        return new RunimoTypeInfo(name, imgUrl, code, description);
    }


    /**
     * EggType enum 클래스 순서 기준으로 EggType 별 러니모 그룹목록 생성
     */
    private static List<RunimoTypeGroup> toRunimoTypeGroups(
        Map<String, List<RunimoTypeInfo>> runimoDtoMap) {

        List<RunimoTypeGroup> runimoTypeGroups = new ArrayList<>();
        for (EggType eggType : EggType.values()) {
            String key = eggType.getName();
            runimoTypeGroups.add(new RunimoTypeGroup(key, runimoDtoMap.get(key)));
        }

        return runimoTypeGroups;
    }

    /**
     * EggType 별 러니모 분류 map 생성
     */
    private static Map<String, List<RunimoTypeInfo>> toRunimoTypeMap(
        List<RunimoTypeSimpleModel> modelList) {

        Map<String, List<RunimoTypeInfo>> runimoTypeDtos = new HashMap<>();
        for (RunimoTypeSimpleModel model : modelList) {
            String key = model.eggType();
            if (!runimoTypeDtos.containsKey(key)) {
                runimoTypeDtos.put(key, new ArrayList<>());
            }
            runimoTypeDtos.get(key).add(model.toDto());
        }

        return runimoTypeDtos;
    }

}

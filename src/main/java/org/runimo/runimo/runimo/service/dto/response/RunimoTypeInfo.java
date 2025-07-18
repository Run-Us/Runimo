package org.runimo.runimo.runimo.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.runimo.runimo.runimo.service.dto.RunimoTypeSimpleModel;

public record RunimoTypeInfo(
    @Schema(description = "러니모 이름", example = "토끼")
    String name,

    @Schema(description = "러니모 이미지 url", example = "http://...")
    String imgUrl,

    @Schema(description = "러니모 code", example = "R-101")
    String code,

    @Schema(description = "러니모 상세설명", example = "마당알에서 태어난 마당 토끼예요.")
    String description
) {

    public static List<RunimoTypeInfo> from (final List<RunimoTypeSimpleModel> modelList) {
        return modelList.stream().map(i ->
            new RunimoTypeInfo(i.getName(), i.getImgUrl(), i.getCode(), i.getDescription())
        ).toList();
    }

}

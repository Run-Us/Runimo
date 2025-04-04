package org.runimo.runimo.runimo.controller.dto.response;

public record RunimoInfo(
        Long id,
        String name,
        String imgUrl,
        String code,
        String eggType,
        String description
) {
}

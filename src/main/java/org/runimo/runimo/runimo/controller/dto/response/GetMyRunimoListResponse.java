package org.runimo.runimo.runimo.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "보유 러니모 목록 조회 응답")
public record GetMyRunimoListResponse(
    List<RunimoInfo> myRunimos
) {
}

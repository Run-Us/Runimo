package org.runimo.runimo.runimo.controller.dto.response;

import java.util.List;

public record GetMyRunimoListResponse(
    List<RunimoInfo> myRunimos
) {
}

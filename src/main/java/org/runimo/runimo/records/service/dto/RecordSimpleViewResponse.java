package org.runimo.runimo.records.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "기록 요약 정보 응답 DTO")
public record RecordSimpleViewResponse(List<RecordSimpleView> recordList) {

}

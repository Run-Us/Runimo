package org.runimo.runimo.records.controller.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.runimo.runimo.records.service.dto.RecordQuery;

@Schema(description = "기록 조회 요청 파라매터")
@Getter
@Setter
public class MyRecordPageRequest {

    @Parameter(description = "페이지 번호", example = "0")
    @Min(0)
    private Integer page = 0;
    @Parameter(description = "페이지 당 크기", example = "10")
    @Min(1)
    @Max(20)
    private Integer size = 10;
    @Parameter(description = "시작 날짜(기본값 : 요청 시점)", example = "2024-04-04")
    private LocalDate startDate = LocalDate.now();
    @Parameter(description = "종료 날짜(기본값 : 요청 시점)", example = "2024-04-04")
    private LocalDate endDate = LocalDate.now();
    @Parameter(description = "정렬 기준", example = "createdAt")
    @Schema(allowableValues = {"startedAt", "createdAt"})
    private String sortBy = "startedAt";
    @Parameter(description = "정렬 방향", example = "asc")
    @Schema(allowableValues = {"asc", "desc"})
    private String sortDirection = "desc";

    public static RecordQuery toQuery(MyRecordPageRequest request, Long userId) {
        return RecordQuery.builder()
            .userId(userId)
            .page(request.getPage())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .size(request.getSize())
            .sortBy(request.getSortBy())
            .sortDirection(request.getSortDirection())
            .build();
    }
}

package org.runimo.runimo.records.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.runimo.runimo.records.service.dto.RecordQuery;

@Getter
@Setter
public class MyRecordPageRequest {

  @Min(0)
  private Integer page = 0;
  @Min(1)
  @Max(20)
  private Integer size = 10;
  private LocalDate startDate = LocalDate.now();
  private LocalDate endDate = LocalDate.now();

  public static RecordQuery toQuery(MyRecordPageRequest request, Long userId) {
    return new RecordQuery(userId, request.getPage(), request.getSize(),
        request.getStartDate(), request.getEndDate());
  }
}

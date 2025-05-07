package org.runimo.runimo.records.service.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class RecordQuery {

  private final Long userId;
  private final Integer page;
  private final Integer size;
  private final LocalDate startDate;
  private final LocalDate endDate;

  public Pageable toPageable() {
    return PageRequest.of(page, size);
  }
}

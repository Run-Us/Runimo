package org.runimo.runimo.records.service.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@Getter
@AllArgsConstructor
public class RecordQuery {

    private final Long userId;
    private final Integer page;
    private final Integer size;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String sortBy;
    private final String sortDirection;

    public Pageable toPageable() {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        return PageRequest.of(page, size, sort);
    }
}

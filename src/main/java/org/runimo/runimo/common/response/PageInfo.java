package org.runimo.runimo.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageInfo {


  private Long totalItems;
  private Integer totalPages;
  private Integer currentPage;
  private Integer perPage;

  public static PageInfo empty(Integer currentPage, Integer perPage) {
    return new PageInfo(0L, 0, currentPage, perPage);
  }


  public static PageInfo of(Page<?> pages) {

    return new PageInfo(pages.getTotalElements(), pages.getTotalPages(), pages.getNumber(), pages.getSize());
  }
}

package org.runimo.runimo.common.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageData<T> {

  private List<T> items;
  private PageInfo pagination;

  public static <T> PageData<T> of(List<T> items, PageInfo pagination) {
    return new PageData<>(items, pagination);
  }
}

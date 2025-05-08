package org.runimo.runimo.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageData<T> {

  @Schema(description = "페이지 데이터 목록")
  private List<T> items;
  @Schema(description = "페이지 정보")
  private PageInfo pagination;

  public static <T> PageData<T> of(List<T> items, PageInfo pagination) {
    return new PageData<>(items, pagination);
  }
}

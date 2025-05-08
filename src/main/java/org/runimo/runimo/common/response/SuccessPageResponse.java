package org.runimo.runimo.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

@Schema(description = "성공 페이지 응답 DTO")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class SuccessPageResponse<T> extends Response {

  @Schema(description = "페이지 데이터")
  private PageData<T> payload;

  public SuccessPageResponse(boolean success, String message, String code,
      PageData<T> payload) {
    super(success, message, code);
    this.payload = payload;
  }

  public SuccessPageResponse(boolean success, CustomResponseCode code,
      PageData<T> payload) {
    super(success, code);
    this.payload = payload;
  }

  public static <T> SuccessPageResponse<T> of(final CustomResponseCode responseCode,
      PageData<T> data) {
    return new SuccessPageResponse<>(true, responseCode, data);
  }

}

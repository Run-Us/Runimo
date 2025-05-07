package org.runimo.runimo.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class SuccessPageResponse<T> extends Response {

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

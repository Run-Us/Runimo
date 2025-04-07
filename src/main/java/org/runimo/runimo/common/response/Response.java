package org.runimo.runimo.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

@Getter
public class Response {

  @Schema(description = "성공 여부")
  private final boolean success;
  @Schema(description = "응답 메세지")
  private final String message;
  @Schema(description = "응답 코드")
  private final String code;

  protected Response(final boolean success, final String message, final String code) {
    this.success = success;
    this.message = message;
    this.code = code;
  }

  protected Response(final boolean success, final CustomResponseCode code) {
    this.success = success;
    this.message = code.getClientMessage();
    this.code = code.getCode();
  }
}


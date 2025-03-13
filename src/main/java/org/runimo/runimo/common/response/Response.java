package org.runimo.runimo.common.response;

import lombok.Getter;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

@Getter
public class Response {

  private final boolean success;
  private final String message;
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


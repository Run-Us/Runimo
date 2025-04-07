package org.runimo.runimo.exceptions;

import lombok.Getter;

@Getter
public class RegisterErrorResponse {
  private boolean success = false;
  private String errorCode;
  private String message;
  private String temporalRegisterToken;

  public RegisterErrorResponse(String errorCode, String message, String temporalRegisterToken) {
    this.errorCode = errorCode;
    this.message = message;
    this.temporalRegisterToken = temporalRegisterToken;
  }
}

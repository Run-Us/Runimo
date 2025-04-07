package org.runimo.runimo.auth.exceptions;

import org.runimo.runimo.exceptions.BusinessException;
import org.runimo.runimo.exceptions.code.CustomResponseCode;


public class UnRegisteredUserException extends BusinessException {
  private final String temporalRegisterToken;

  protected UnRegisteredUserException(CustomResponseCode errorCode, String temporalRegisterToken) {
    super(errorCode);
    this.temporalRegisterToken = temporalRegisterToken;
  }

  protected UnRegisteredUserException(CustomResponseCode errorCode, String logMessage, String temporalRegisterToken) {
    super(errorCode, logMessage);
    this.temporalRegisterToken = temporalRegisterToken;
  }

  public static UnRegisteredUserException of(CustomResponseCode errorCode, String token) {
    return new UnRegisteredUserException(errorCode, token);
  }

  public String getTemporalRegisterToken() {
    return temporalRegisterToken;
  }
}

package org.runimo.runimo.user.exceptions;

import org.runimo.runimo.exceptions.BusinessException;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

public class SignUpException extends BusinessException {

  public SignUpException(CustomResponseCode errorCode) {
    super(errorCode);
  }

  protected SignUpException(CustomResponseCode errorCode, String logMessage) {
    super(errorCode, logMessage);
  }
}

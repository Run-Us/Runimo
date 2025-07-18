package org.runimo.runimo.auth.exceptions;

import org.runimo.runimo.exceptions.BusinessException;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

public class UserJwtException extends BusinessException {

    protected UserJwtException(CustomResponseCode errorCode) {
        super(errorCode);
    }

    public UserJwtException(CustomResponseCode errorCode, String logMessage) {
        super(errorCode, logMessage);
    }

    public static UserJwtException of(CustomResponseCode errorCode) {
        return new UserJwtException(errorCode, errorCode.getLogMessage());
    }
}

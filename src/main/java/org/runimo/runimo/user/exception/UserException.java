package org.runimo.runimo.user.exception;

import org.runimo.runimo.exceptions.BusinessException;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

public class UserException extends BusinessException {

    protected UserException(CustomResponseCode errorCode) {
        super(errorCode);
    }

    public UserException(CustomResponseCode errorCode, String logMessage) {
        super(errorCode, logMessage);
    }

    public static UserException of(CustomResponseCode errorCode) {
        return new UserException(errorCode, errorCode.getLogMessage());
    }
}

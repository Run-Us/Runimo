package org.runimo.runimo.runimo.exception;

import org.runimo.runimo.exceptions.BusinessException;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

public class RunimoException extends BusinessException {

    protected RunimoException(CustomResponseCode errorCode) {
        super(errorCode);
    }

    protected RunimoException(CustomResponseCode errorCode, String logMessage) {
        super(errorCode, logMessage);
    }

    public static RunimoException of(CustomResponseCode errorCode) {
        return new RunimoException(errorCode, errorCode.getLogMessage());
    }
}

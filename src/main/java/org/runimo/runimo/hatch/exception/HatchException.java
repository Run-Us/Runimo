package org.runimo.runimo.hatch.exception;

import org.runimo.runimo.exceptions.BusinessException;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

public class HatchException extends BusinessException {

    protected HatchException(CustomResponseCode errorCode) {
        super(errorCode);
    }

    protected HatchException(CustomResponseCode errorCode, String logMessage) {
        super(errorCode, logMessage);
    }

    public static HatchException of(CustomResponseCode errorCode) {
        return new HatchException(errorCode, errorCode.getLogMessage());
    }
}

package org.runimo.runimo.external;

import org.runimo.runimo.exceptions.BusinessException;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

public class ExternalServiceException extends BusinessException {

    protected ExternalServiceException(CustomResponseCode errorCode) {
        super(errorCode);
    }

    public static ExternalServiceException of(CustomResponseCode errorCode) {
        return new ExternalServiceException(errorCode);
    }
}

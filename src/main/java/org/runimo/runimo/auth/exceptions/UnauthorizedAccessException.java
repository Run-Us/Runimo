package org.runimo.runimo.auth.exceptions;

import org.runimo.runimo.auth.filters.UserErrorCode;
import org.runimo.runimo.exceptions.BusinessException;

public class UnauthorizedAccessException extends BusinessException {

    public UnauthorizedAccessException(String message) {
        super(UserErrorCode.INSUFFICIENT_PERMISSIONS, message);
    }

    public UnauthorizedAccessException(Throwable cause) {
        super(UserErrorCode.INSUFFICIENT_PERMISSIONS, cause.getMessage());
    }
}

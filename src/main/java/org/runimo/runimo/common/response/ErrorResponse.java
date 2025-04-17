package org.runimo.runimo.common.response;

import lombok.ToString;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

@ToString
public class ErrorResponse extends Response {

    private ErrorResponse(final String errorMessage, final String errorCode) {
        super(false, errorMessage, errorCode);
    }

    private ErrorResponse(final CustomResponseCode errorCode) {
        super(false, errorCode);
    }

    public static ErrorResponse of(final String errorMessage, final String errorCode) {
        return new ErrorResponse(errorMessage, errorCode);
    }

    public static ErrorResponse of(final CustomResponseCode errorCode) {
        return new ErrorResponse(errorCode);
    }
}


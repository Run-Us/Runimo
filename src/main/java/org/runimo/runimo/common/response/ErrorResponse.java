package org.runimo.runimo.common.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse extends Response {

    protected ErrorResponse(final String errorMessage, final String errorCode) {
        super(false, errorMessage, errorCode);
    }

    public static ErrorResponse of(final String errorMessage, final String errorCode) {
        return new ErrorResponse(errorMessage, errorCode);
    }

    public static ErrorResponse of(final CustomResponseCode errorCode) {
        return new ErrorResponse(errorCode.getClientMessage(), errorCode.getCode());
    }
}


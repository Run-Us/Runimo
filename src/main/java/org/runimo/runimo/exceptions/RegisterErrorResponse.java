package org.runimo.runimo.exceptions;

import lombok.Getter;
import org.runimo.runimo.common.response.Response;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

@Getter
public class RegisterErrorResponse extends Response {

    private final String temporalRegisterToken;

    public RegisterErrorResponse(CustomResponseCode errorCode, String temporalRegisterToken) {
        super(false, errorCode.getClientMessage(), errorCode.getCode());
        this.temporalRegisterToken = temporalRegisterToken;
    }
}

package org.runimo.runimo.exceptions;

import lombok.Getter;

@Getter
public class RegisterErrorResponse {

    private final boolean success = false;
    private final String errorCode;
    private final String message;
    private final String temporalRegisterToken;

    public RegisterErrorResponse(String errorCode, String message, String temporalRegisterToken) {
        this.errorCode = errorCode;
        this.message = message;
        this.temporalRegisterToken = temporalRegisterToken;
    }
}

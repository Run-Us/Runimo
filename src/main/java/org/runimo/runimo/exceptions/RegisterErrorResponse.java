package org.runimo.runimo.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.runimo.runimo.common.response.ErrorResponse;
import org.runimo.runimo.exceptions.code.CustomResponseCode;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterErrorResponse extends ErrorResponse {

    private  String temporalRegisterToken;

    public RegisterErrorResponse(CustomResponseCode errorCode, String temporalRegisterToken) {
        super(errorCode.getClientMessage(), errorCode.getCode());
        this.temporalRegisterToken = temporalRegisterToken;
    }
}

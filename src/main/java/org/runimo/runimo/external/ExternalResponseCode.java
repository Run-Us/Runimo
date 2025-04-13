package org.runimo.runimo.external;

import org.runimo.runimo.exceptions.code.CustomResponseCode;
import org.springframework.http.HttpStatus;

public enum ExternalResponseCode implements CustomResponseCode {
    PRESIGNED_URL_FETCHED(HttpStatus.CREATED, "Presigned URL 발급 성공", "Presigned URL 발급 성공");

    private final HttpStatus code;
    private final String clientMessage;
    private final String logMessage;

    ExternalResponseCode(HttpStatus httpStatus, String clientMessage, String logMessage) {
        this.code = httpStatus;
        this.clientMessage = clientMessage;
        this.logMessage = logMessage;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    @Override
    public String getClientMessage() {
        return this.clientMessage;
    }

    @Override
    public String getLogMessage() {
        return this.logMessage;
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return this.code;
    }
}

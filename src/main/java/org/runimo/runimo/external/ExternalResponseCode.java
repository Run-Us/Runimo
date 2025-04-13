package org.runimo.runimo.external;

import org.runimo.runimo.exceptions.code.CustomResponseCode;
import org.springframework.http.HttpStatus;

public enum ExternalResponseCode implements CustomResponseCode {
    PRESIGNED_URL_FETCHED("ESH2011", HttpStatus.CREATED, "Presigned URL 발급 성공", "Presigned URL 발급 성공"),

    PRESIGNED_URL_FETCH_FAILED("ESH4001", HttpStatus.BAD_REQUEST, "Presigned URL 발급 실패", "Presigned URL 발급 실패");
    private final String code;
    private final HttpStatus httpStatus;
    private final String clientMessage;
    private final String logMessage;

    ExternalResponseCode(String code, HttpStatus httpStatus, String clientMessage, String logMessage) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.clientMessage = clientMessage;
        this.logMessage = logMessage;
    }

    @Override
    public String getCode() {
        return this.code;
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
        return this.httpStatus;
    }
}

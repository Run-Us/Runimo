package org.runimo.runimo.runimo.exception;

import org.runimo.runimo.exceptions.code.CustomResponseCode;
import org.springframework.http.HttpStatus;

public enum RunimoHttpResponseCode implements CustomResponseCode {
    GET_MY_RUNIMO_LIST_SUCCESS("MSH2001", "보유 러니모 목록 조회 성공", "보유 러니모 목록 조회 성공", HttpStatus.OK),
    SET_MAIN_RUNIMO_SUCCESS("MSH2002", "대표 러니모 설정 성공", "대표 러니모 설정 성공", HttpStatus.OK),
    GET_ALL_RUNIMO_TYPE_LIST_SUCCESS("MSH2003", "전체 러니모 종류 조회 성공", "전체 러니모 종류 조회 성공",
        HttpStatus.OK),

    USER_DO_NOT_OWN_RUNIMO("MSH4031", "요청 러니모의 소유자가 아님", "요청 러니모의 소유자가 아님", HttpStatus.FORBIDDEN),
    RUNIMO_NOT_FOUND("MSH4041", "요청 러니모가 존재하지 않음", "요청 러니모가 존재하지 않음", HttpStatus.NOT_FOUND),
    ;

    private final String code;
    private final String clientMessage;
    private final String logMessage;
    private final HttpStatus httpStatus;

    RunimoHttpResponseCode(String code, String clientMessage, String logMessage,
        HttpStatus httpStatus) {
        this.code = code;
        this.clientMessage = clientMessage;
        this.logMessage = logMessage;
        this.httpStatus = httpStatus;
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
        return httpStatus;
    }
}


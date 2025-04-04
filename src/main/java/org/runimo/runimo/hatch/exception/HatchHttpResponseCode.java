package org.runimo.runimo.hatch.exception;

import org.runimo.runimo.exceptions.code.CustomResponseCode;
import org.springframework.http.HttpStatus;

public enum HatchHttpResponseCode implements CustomResponseCode {
  HATCH_EGG_SUCCESS("HSH2018","알 부화 성공" , "알 부화 성공", HttpStatus.CREATED),

  HATCH_EGG_NOT_FOUND("HEH4041", "부화 요청 알이 존재하지 않음", "부화 요청 알이 존재하지 않음", HttpStatus.NOT_FOUND),
  HATCH_EGG_NOT_READY("HEH4002", "부화 요청 알이 부화 가능한 상태가 아님", "부화 요청 알이 부화 가능한 상태가 아님", HttpStatus.BAD_REQUEST)
  ;


  private final String code;
  private final String clientMessage;
  private final String logMessage;
  private final HttpStatus httpStatus;

  HatchHttpResponseCode(String code, String clientMessage, String logMessage, HttpStatus httpStatus) {
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


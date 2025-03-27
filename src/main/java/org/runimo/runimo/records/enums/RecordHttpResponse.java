package org.runimo.runimo.records.enums;
import org.runimo.runimo.exceptions.code.CustomResponseCode;
import org.springframework.http.HttpStatus;

public enum RecordHttpResponse implements CustomResponseCode {
  RECORD_SAVED("RSH2001", "달리기 기록 저장 성공", "달리기 기록 저장 성공"),
  ;

  private final String code;
  private final String clientMessage;
  private final String logMessage;

  RecordHttpResponse(String code, String clientMessage, String logMessage) {
    this.code = code;
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
    return null;
  }
}



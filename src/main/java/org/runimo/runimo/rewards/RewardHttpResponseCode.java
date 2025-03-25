package org.runimo.runimo.rewards;

import org.runimo.runimo.exceptions.code.CustomResponseCode;
import org.springframework.http.HttpStatus;

public enum RewardHttpResponseCode implements CustomResponseCode {
  CLAIM_REWARD_SUCESS("RWH2011", "보상 발급 성공", "보상 발급 성공"),
  ;

  private final String code;
  private final String clientMessage;
  private final String logMessage;

  RewardHttpResponseCode(String code, String clientMessage, String logMessage) {
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



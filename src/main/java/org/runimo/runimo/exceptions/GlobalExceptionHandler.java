package org.runimo.runimo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.runimo.runimo.common.response.ErrorResponse;
import org.runimo.runimo.user.exceptions.SignUpException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
    log.debug("ERROR: {}}", e.getMessage(), e);
    return ResponseEntity.status(e.getHttpStatusCode()).body(ErrorResponse.of(e.getMessage(), e.getErrorCode().getCode()));
  }

  @ExceptionHandler(SignUpException.class)
  public ResponseEntity<ErrorResponse> handleSignUpException(SignUpException e) {
    log.debug("ERROR: {}}", e.getMessage(), e);
    return ResponseEntity.badRequest().body(ErrorResponse.of(e.getErrorCode()));
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
    log.debug("ERROR: {}}", e.getMessage(), e);
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    log.debug("ERROR: {}}", e.getMessage(), e);
    return ResponseEntity.badRequest().body(ErrorResponse.of("잘못된 요청입니다.", e.getMessage()));
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
    log.debug("ERROR: {}}", e.getMessage(), e);
    return ResponseEntity.badRequest().body(ErrorResponse.of("잘못된 요청입니다.", e.getMessage()));
  }
}

package org.runimo.runimo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.runimo.runimo.common.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
    log.debug("ERROR: {}}", e.getMessage(), e);
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
    log.debug("ERROR: {}}", e.getMessage(), e);
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
    log.debug("ERROR: {}}", e.getMessage(), e);
    return ResponseEntity.badRequest().build();
  }
}

package org.runimo.runimo.exceptions;

import jakarta.persistence.LockTimeoutException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.runimo.runimo.auth.exceptions.SignUpException;
import org.runimo.runimo.auth.exceptions.UnRegisteredUserException;
import org.runimo.runimo.auth.exceptions.UserJwtException;
import org.runimo.runimo.common.response.ErrorResponse;
import org.runimo.runimo.external.ExternalServiceException;
import org.runimo.runimo.hatch.exception.HatchException;
import org.runimo.runimo.runimo.exception.RunimoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_LOG_HEADER = "ERROR: ";


    @ExceptionHandler(RunimoException.class)
    public ResponseEntity<ErrorResponse> handleRunimoException(RunimoException e) {
        log.warn("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatusCode())
            .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(HatchException.class)
    public ResponseEntity<ErrorResponse> handleHatchException(HatchException e) {
        log.warn("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatusCode())
            .body(ErrorResponse.of(e.getErrorCode()));
    }


    @ExceptionHandler(UserJwtException.class)
    public ResponseEntity<ErrorResponse> handleUserJwtException(UserJwtException e) {
        log.warn("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatusCode())
            .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(UnRegisteredUserException.class)
    public ResponseEntity<RegisterErrorResponse> handleUnRegisteredUserException(
        UnRegisteredUserException e) {
        log.warn("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatusCode()).body(
            new RegisterErrorResponse(
                e.getErrorCode().getCode(),
                e.getMessage(),
                e.getTemporalRegisterToken()
            ));
    }

    @ExceptionHandler(SignUpException.class)
    public ResponseEntity<ErrorResponse> handleSignUpException(SignUpException e) {
        log.warn("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatusCode())
            .body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
        MethodArgumentNotValidException e) {
        log.warn("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        StringBuilder detailMessage = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            detailMessage.append(fieldName).append(": ").append(errorMessage).append(", ");
        });
        String details = !detailMessage.isEmpty() ?
            detailMessage.substring(0, detailMessage.length() - 2) : "";
        return ResponseEntity.badRequest().body(ErrorResponse.of("유효성 검증에 실패했습니다.", details));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
        MissingServletRequestParameterException e) {
        log.warn("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.badRequest().body(
            ErrorResponse.of("필수 파라미터가 누락되었습니다.",
                "파라미터 '" + e.getParameterName() + "'이(가) 필요합니다."));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException e) {
        log.warn("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
            ErrorResponse.of("지원하지 않는 HTTP 메소드입니다.",
                e.getMethod() + " 메소드는 지원되지 않습니다."));
    }

    /**
     * XLOCK 타임아웃 에러 처리기 타임아웃이 발생하면 LockTimeoutException이 발생한다.
     * TODO : 타임아웃 발생시 디스코드로 알림.
     */
    @ExceptionHandler(LockTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleLockTimeoutException(LockTimeoutException e) {
        log.error("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse.of("잠시 후 다시 시도해주세요", "대기 시간 초과"));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        log.warn("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException e) {
        log.warn("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of("잘못된 요청입니다.", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        log.warn("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of("잘못된 요청입니다.", e.getMessage()));
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(
        ExternalServiceException e) {
        log.error("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.status(e.getHttpStatusCode())
            .body(ErrorResponse.of(e.getErrorCode()));
    }

    // Root 서비스 에러 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse.of("internal server error", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("{} {}", ERROR_LOG_HEADER, e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse.of("internal server error", "internal server error"));
    }
}

package com.orderingsystem.orderingsystem.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.orderingsystem.orderingsystem.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /* ---------- 400 Bad Request & Validation ---------- */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest req) {

        List<ViolationResponse> details = ex.getBindingResult()
                                    .getFieldErrors()
                                    .stream()
                                    .map(this::mapViolation)
                                    .toList();

        return build(HttpStatus.BAD_REQUEST, "Validation failed", req, details);
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            InvalidFormatException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MissingPathVariableException.class
    })
    public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req);
    }

    /* ---------- 401 / 403 ---------- */

    @ExceptionHandler({
            BadCredentialsException.class,
            UsernameNotFoundException.class,
            AuthenticationCredentialsNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleAuthFail(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Wrong username or password", req);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleForbidden(AccessDeniedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "Access denied", req);
    }

    /* ---------- 404 ---------- */

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    /* ---------- 405 / 415 / 429 ---------- */

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
                                                           HttpServletRequest req) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), req);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnsupportedMedia(HttpMediaTypeNotSupportedException ex,
                                                           HttpServletRequest req) {
        return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage(), req);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleFileTooLarge(MaxUploadSizeExceededException ex, HttpServletRequest req) {
        return build(HttpStatus.PAYLOAD_TOO_LARGE, "File size exceeds limit", req);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleRateLimit(RateLimitExceededException ex, HttpServletRequest req) {
        return build(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage(), req);
    }

    /* ---------- 409 / 422 ---------- */

    @ExceptionHandler({ DataIntegrityViolationException.class, ConstraintViolationException.class })
    public ResponseEntity<ApiErrorResponse> handleConflict(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "Database constraint violated", req);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiErrorResponse> handleUnprocessable(BusinessRuleException ex, HttpServletRequest req) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), req);
    }

    @ExceptionHandler(UsernameException.class)
    public ResponseEntity<ApiErrorResponse> handleUsernameTaken(UsernameException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), req);
    }


    /* ---------- 500 / 503 ---------- */

    @ExceptionHandler({ SQLException.class, IllegalStateException.class })
    public ResponseEntity<ApiErrorResponse> handleServer(Exception ex, HttpServletRequest req) {
        log.error("Internal error", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error, please contact support", req);
    }

    @ExceptionHandler(UpstreamServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleServiceUnavailable(UpstreamServiceException ex, HttpServletRequest req) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), req);
    }

    /* ---------- fallback ---------- */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAll(Exception ex, HttpServletRequest req) {
        log.error("Unhandled error", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error, please contact support", req);
    }

    /* ---------- mapping helper ---------- */

    private ViolationResponse mapViolation(FieldError f) {
        return new ViolationResponse(f.getField(), f.getRejectedValue(), f.getDefaultMessage());
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, HttpServletRequest req) {
        return build(status, message, req, Collections.emptyList());
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status,
                                           String message,
                                           HttpServletRequest req,
                                           List<ViolationResponse> violations) {

        ApiErrorResponse body = ApiErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(req.getRequestURI())
                .violations(violations.isEmpty() ? null : violations)
                .build();

        return ResponseEntity.status(status).body(body);
    }
}

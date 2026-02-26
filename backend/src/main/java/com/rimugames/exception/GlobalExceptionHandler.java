package com.rimugames.exception;

import com.rimugames.common.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleException(BusinessException e) {
        logger.error("Business Exception : ", e);

        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.failure(errorCode.getMessage(), errorCode.getCode());

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleException(MethodArgumentNotValidException e) {
        logger.error("MethodArgumentNotValidException : ", e);

        ErrorCode errorCode = ErrorCode.INVALID_INPUT;
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiResponse<Map<String, String>> response = ApiResponse.failure("입력값 검증에 실패하였습니다.", errorCode.getCode(), errors);

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        logger.error("Unhandled Exception : ", e);

        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        ApiResponse<String> response = ApiResponse.failure(
                errorCode.getMessage(),
                errorCode.getCode(),
                e.getMessage() + " / caused by: " + (e.getCause() != null ? e.getCause().getMessage() : "null")
        );

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }
}

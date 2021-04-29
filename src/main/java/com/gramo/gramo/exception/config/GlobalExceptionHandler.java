package com.gramo.gramo.exception.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    ResponseEntity<ErrorResponse> handleGlobalException(GlobalException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return new ResponseEntity<>(new ErrorResponse(errorCode.getReason(), errorCode.getStatus()),
                HttpStatus.valueOf(errorCode.getStatus()));
    }

}

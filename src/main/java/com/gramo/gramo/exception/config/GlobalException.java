package com.gramo.gramo.exception.config;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private ErrorCode errorCode;

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getReason());
        this.errorCode = errorCode;
    }
}

package com.gramo.gramo.exception;

import com.gramo.gramo.exception.config.ErrorCode;
import com.gramo.gramo.exception.config.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class PermissionMismatchException extends GlobalException {
    public PermissionMismatchException() {
        super(ErrorCode.INVALID_ACCESS);
    }
}

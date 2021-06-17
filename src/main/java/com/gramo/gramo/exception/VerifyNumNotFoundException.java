package com.gramo.gramo.exception;

import com.gramo.gramo.exception.config.ErrorCode;
import com.gramo.gramo.exception.config.GlobalException;

public class VerifyNumNotFoundException extends GlobalException {
    public VerifyNumNotFoundException() {
        super(ErrorCode.EMAIL_CODE_NOT_FOUND);
    }
}

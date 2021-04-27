package com.gramo.gramo.exception;

import com.gramo.gramo.exception.config.ErrorCode;
import com.gramo.gramo.exception.config.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class PicuNotFoundException extends GlobalException {
    public PicuNotFoundException() {
        super(ErrorCode.PICU_NOT_FOUND);
    }
}

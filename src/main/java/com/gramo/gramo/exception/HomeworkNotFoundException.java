package com.gramo.gramo.exception;

import com.gramo.gramo.exception.config.ErrorCode;
import com.gramo.gramo.exception.config.GlobalException;

public class HomeworkNotFoundException extends GlobalException {
    public HomeworkNotFoundException() {
        super(ErrorCode.HOMEWORK_NOT_FOUND);
    }
}

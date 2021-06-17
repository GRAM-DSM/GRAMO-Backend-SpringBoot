package com.gramo.gramo.exception;

import com.gramo.gramo.exception.config.ErrorCode;
import com.gramo.gramo.exception.config.GlobalException;

public class InvalidCalendarAccessException extends GlobalException {
    public InvalidCalendarAccessException() {
        super(ErrorCode.INVALID_CALENDAR_ACCESS);
    }
}

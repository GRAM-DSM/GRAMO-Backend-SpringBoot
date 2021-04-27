package com.gramo.gramo.exception;

import com.gramo.gramo.exception.config.ErrorCode;
import com.gramo.gramo.exception.config.GlobalException;

public class SendNotificationFailed extends GlobalException {
    public SendNotificationFailed() {
        super(ErrorCode.NOTIFICATION_SEND_FAILED);
    }
}

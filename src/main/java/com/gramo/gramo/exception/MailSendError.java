package com.gramo.gramo.exception;

import com.gramo.gramo.exception.config.ErrorCode;
import com.gramo.gramo.exception.config.GlobalException;

public class MailSendError extends GlobalException {
    public MailSendError() {
        super(ErrorCode.MAIL_SEND_ERROR);
    }
}

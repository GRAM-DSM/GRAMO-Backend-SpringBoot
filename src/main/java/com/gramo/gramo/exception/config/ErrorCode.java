package com.gramo.gramo.exception.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST("Bad Request", 400),
    NOT_FOUND("Not Found", 404),
    INTERNAL_SERVER_ERROR("Internal Server Error", 500),
    HOMEWORK_NOT_FOUND("Homework Not Found", 404),
    INVALID_TOKEN("Invalid Token", 401),
    INVALID_ACCESS("Invalid Access", 401),
    INVALID_CALENDAR_ACCESS("Invalid Calendar Access", 403),
    NOT_ASSIGNED("Not Assigned Homework", 409),
    USER_ALREADY_EXIST("User Already Exist", 409),
    MAIL_SEND_ERROR("Mail Send Error", 500),
    EMAIL_CODE_NOT_FOUND("Email Code Not Found", 404),
    PLAN_NOT_FOUND("Plan Not Found", 404),
    PICU_NOT_FOUND("Picu Not Found", 404),
    USER_NOT_FOUND("User Not Found", 404),
    NOTICE_NOT_FOUND("Notice Not Found", 404),
    NOTIFICATION_SEND_FAILED("Notification Send Failed", 500);

    private final String reason;

    private final Integer status;
}

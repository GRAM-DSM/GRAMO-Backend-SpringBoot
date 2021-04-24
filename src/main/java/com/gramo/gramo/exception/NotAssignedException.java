package com.gramo.gramo.exception;

import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Not Assigned")
public class NotAssignedException extends RuntimeException {
    public NotAssignedException() {
        super("NOT_ASSIGNED_EXCEPTION");
    }
}

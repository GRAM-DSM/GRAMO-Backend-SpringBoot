package com.gramo.gramo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Permission Mismatch")
public class PermissionMismatchException extends RuntimeException {
}

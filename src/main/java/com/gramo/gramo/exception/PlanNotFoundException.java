package com.gramo.gramo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Plan Not Found")
public class PlanNotFoundException extends RuntimeException{
}

package com.nomoney.paymybuddy.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class for indicating that the operation is not allowed.
 * This exception is thrown when a user tries to perform an action that they are not authorized to perform.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class OperationNotAllowedException extends RuntimeException {
    public OperationNotAllowedException(String message) {
        super(message);
    }
}

package com.nomoney.paymybuddy.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class representing a conflict in the data being sent to the server.
 * It is thrown when a resource already exists in the system.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class DataAlreadyExistException extends RuntimeException {
    public DataAlreadyExistException(String message) {
        super(message);
    }
}

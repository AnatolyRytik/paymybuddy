package com.nomoney.paymybuddy.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class that indicates a conflict with the current state of the target resource.
 * This exception is thrown when there is not enough money in user's account.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}

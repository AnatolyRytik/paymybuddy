package com.nomoney.paymybuddy.dto;

import lombok.Data;

import javax.validation.constraints.Email;

/**
 * This class is the DTO representation of an internal transaction.
 * It holds the amount, userEmail and emailRecipient of the transaction.
 */
@Data
public class InternalTransactionDto {

    private double amount;

    @Email
    private String userEmail;

    @Email
    private String emailRecipient;
}

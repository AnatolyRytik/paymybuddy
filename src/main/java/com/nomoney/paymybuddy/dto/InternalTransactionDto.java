package com.nomoney.paymybuddy.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * This class is the DTO representation of an internal transaction.
 * It holds the amount, userEmail and emailRecipient of the transaction.
 */
@Data
public class InternalTransactionDto {

    @Pattern(regexp = "^[+-]?(([1-9]\\d*)|0)(\\.\\d+)?")
    private double amount;

    @Email
    private String userEmail;

    @Email
    private String emailRecipient;

    @NotBlank
    private String description;
}

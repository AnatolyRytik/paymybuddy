package com.nomoney.paymybuddy.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * The class ExternalTransactionDto is used for transfering funds to or from external bank account.
 *
 * @param amountToAdd      the amount to be added to the account
 * @param amountToWithdraw the amount to be withdrawn from the account
 * @param userEmail        the email of the user making the transaction
 * @param userIban         the IBAN number of the user making the transaction
 */
@Data
public class ExternalTransactionDto {

    @Pattern(regexp = "^[+-]?(([1-9]\\d*)|0)(\\.\\d+)?")
    private double amountToAdd = 0.0;

    @Pattern(regexp = "^[+-]?(([1-9]\\d*)|0)(\\.\\d+)?")
    private double amountToWithdraw = 0.0;

    @Email
    private String userEmail;

    @NotBlank
    private String userIban;

}

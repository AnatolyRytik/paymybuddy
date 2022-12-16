package com.nomoney.paymybuddy.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class InternalTransactionDto {
    @NotBlank
    private double amount;

    @Email
    private String userEmail;

    @Email
    private String emailRecipient;
}

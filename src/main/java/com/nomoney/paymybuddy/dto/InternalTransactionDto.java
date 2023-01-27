package com.nomoney.paymybuddy.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class InternalTransactionDto {

    private double amount;

    @Email
    private String userEmail;

    @Email
    private String emailRecipient;
}

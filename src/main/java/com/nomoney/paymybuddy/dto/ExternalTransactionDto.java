package com.nomoney.paymybuddy.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ExternalTransactionDto {
    @NotBlank
    private double amount;

    @Email
    private String userEmail;

    private String userIban;

}

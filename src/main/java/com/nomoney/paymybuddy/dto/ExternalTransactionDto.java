package com.nomoney.paymybuddy.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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

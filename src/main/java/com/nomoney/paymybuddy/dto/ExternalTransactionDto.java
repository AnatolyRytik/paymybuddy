package com.nomoney.paymybuddy.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ExternalTransactionDto {

    @Pattern(regexp = "^[+-]?(([1-9]\\d*)|0)(\\.\\d+)?")
    private double amountToAdd;

    @Pattern(regexp = "^[+-]?(([1-9]\\d*)|0)(\\.\\d+)?")
    private double amountToWithdraw;

    @Email
    private String userEmail;

    private String userIban;

}

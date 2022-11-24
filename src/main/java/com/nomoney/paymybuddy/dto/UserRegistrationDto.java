package com.nomoney.paymybuddy.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String firstname;
    private String lastname;
    private String password;
    private String email;
}

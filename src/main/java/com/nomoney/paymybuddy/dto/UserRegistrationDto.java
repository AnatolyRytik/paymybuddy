package com.nomoney.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * The UserRegistrationDto is a data transfer object (DTO) that holds the information for a user registration.
 * It contains the first name, last name, password and email of the user.
 */

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRegistrationDto {
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    private String password;
    @Email
    private String email;
}

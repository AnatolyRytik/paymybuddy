package com.nomoney.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents a Data Transfer Object (DTO) for the contact form.
 * It contains fields for storing a user's email and a friend's email.
 */
@Data
@AllArgsConstructor
public class ContactFormDto {
    private String userEmail;
    private String friendEmail;
}

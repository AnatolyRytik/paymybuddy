package com.nomoney.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactFormDto {
    private String userEmail;
    private String friendEmail;
}

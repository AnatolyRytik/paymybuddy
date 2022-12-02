package com.nomoney.paymybuddy.dto;

import com.nomoney.paymybuddy.model.Contact;
import lombok.Data;

@Data
public class ContactFormDto {
    private String userEmail;
    private String friendEmail;

    public ContactFormDto(Contact contact) {
        this.userEmail = contact.getUser().getEmail();
        this.friendEmail = contact.getFriend().getEmail();
    }
}

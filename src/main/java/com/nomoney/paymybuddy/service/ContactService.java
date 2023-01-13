package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.ContactFormDto;
import com.nomoney.paymybuddy.model.Contact;

import java.util.List;

public interface ContactService {

    ContactFormDto addFriend(ContactFormDto contactFormDto);

    List<Contact> getContacts(String userEmail);

    Boolean deleteContact(Long id);
}

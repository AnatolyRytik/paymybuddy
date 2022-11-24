package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.ContactFormDto;
import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import com.nomoney.paymybuddy.model.Contact;
import com.nomoney.paymybuddy.model.User;

import java.util.Set;

public interface UserService {
    User saveUser(UserRegistrationDto userRegistrationDto);

    User addFriend(ContactFormDto contactFormDto);

    Set<Contact> contacts(String userEmail);

    Boolean deleteContact(String email);
}

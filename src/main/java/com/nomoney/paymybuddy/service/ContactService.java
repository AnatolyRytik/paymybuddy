package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.ContactFormDto;
import com.nomoney.paymybuddy.model.Contact;

import java.util.List;

/**
 * The ContactService interface provides methods for managing a user's contacts.
 * <p>
 * It includes methods for adding, retrieving, and deleting contacts.
 */
public interface ContactService {

    /**
     * This method adds a friend to a user's contact list.
     *
     * @param contactFormDto a ContactFormDto object that contains the contact's email and the user's email.
     * @return A ContactFormDto object that contains the contact's email and the user's email.
     */
    ContactFormDto addFriend(ContactFormDto contactFormDto);

    /**
     * This method retrieves all contacts of a user.
     *
     * @param userEmail A String that contains the user's email.
     * @return A List of Contact objects that represent the user's contacts.
     */
    List<Contact> getContacts(String userEmail);
}

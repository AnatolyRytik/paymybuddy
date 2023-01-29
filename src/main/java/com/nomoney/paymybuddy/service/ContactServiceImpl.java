package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.ContactFormDto;
import com.nomoney.paymybuddy.model.Contact;
import com.nomoney.paymybuddy.model.User;
import com.nomoney.paymybuddy.repository.ContactRepository;
import com.nomoney.paymybuddy.repository.UserRepository;
import com.nomoney.paymybuddy.util.exception.DataAlreadyExistException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class that handles all Contact related business logic.
 */
@Slf4j
@Service
public class ContactServiceImpl implements ContactService {
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;

    public ContactServiceImpl(UserRepository userRepository, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
    }

    /**
     * Adds a new friend to the user's contact list.
     *
     * @param contactFormDto form data for the new contact, including the user's email and friend's email
     * @return the contact form data
     * @throws DataAlreadyExistException if a contact between the two users already exists
     * @throws NotFoundException         if the user or friend could not be found in the database
     */
    @Override
    public ContactFormDto addFriend(ContactFormDto contactFormDto) {
        log.debug("Adding friend with email {} to user with email {}", contactFormDto.getFriendEmail(), contactFormDto.getUserEmail());
        Optional<User> user = userRepository.findByEmail(contactFormDto.getUserEmail());
        Optional<User> friend = userRepository.findByEmail(contactFormDto.getFriendEmail());
        if (user.isPresent() && friend.isPresent()) {
            List<Contact> allUserContacts = contactRepository.findAllByUserEmail(user.get().getEmail());
            Contact contact = new Contact(user.get(), friend.get());
            if (allUserContacts.contains(contact)) {
                log.error("Contact already exists");
                throw new DataAlreadyExistException("Contact already exists");
            }
            contactRepository.save(contact);
            log.debug("Friend added successfully");
        } else {
            log.error("User not found");
            throw new NotFoundException("User not found");
        }
        return contactFormDto;
    }

    /**
     * Retrieves the contacts for a specific user.
     *
     * @param userEmail the email of the user whose contacts are to be retrieved
     * @return a list of the user's contacts
     */
    @Override
    public List<Contact> getContacts(String userEmail) {
        log.debug("Retrieving contacts for user with email {}", userEmail);
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            log.debug("Friend list successfully found");
            return contactRepository.findAllByUserEmail(userEmail);
        } else {
            log.error("User not found");
            throw new NotFoundException("User not found");
        }
    }

}

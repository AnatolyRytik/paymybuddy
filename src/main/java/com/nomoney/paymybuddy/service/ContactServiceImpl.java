package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.ContactFormDto;
import com.nomoney.paymybuddy.model.Contact;
import com.nomoney.paymybuddy.model.User;
import com.nomoney.paymybuddy.repository.ContactRepository;
import com.nomoney.paymybuddy.repository.UserRepository;
import com.nomoney.paymybuddy.util.exception.DataAlreadyExistException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;

    public ContactServiceImpl(UserRepository userRepository, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    public ContactFormDto addFriend(ContactFormDto contactFormDto) {
        Optional<User> user = userRepository.findByEmail(contactFormDto.getUserEmail());
        Optional<User> friend = userRepository.findByEmail(contactFormDto.getFriendEmail());
        if (user.isPresent() && friend.isPresent()) {
            List<Contact> allUserContacts = contactRepository.findAllByUserEmail(user.get().getEmail());
            Contact contact = new Contact(user.get(), friend.get());
            if (allUserContacts.contains(contact)) {
                throw new DataAlreadyExistException("Contact already exists");
            }
            contactRepository.save(contact);
        } else throw new NotFoundException("User not found");
        return contactFormDto;
    }

    @Override
    public List<Contact> getContacts(String userEmail) {
        return contactRepository.findAllByUserEmail(userEmail);
    }

    @Override
    public Boolean deleteContact(String email) {
        return null;
    }

}

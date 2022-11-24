package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.ContactFormDto;
import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import com.nomoney.paymybuddy.model.Contact;
import com.nomoney.paymybuddy.model.User;
import com.nomoney.paymybuddy.repository.ContactRepository;
import com.nomoney.paymybuddy.repository.UserRepository;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ContactRepository contactRepository;

    public UserServiceImpl(UserRepository userRepository, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    public User saveUser(UserRegistrationDto userRegistrationDto) {
        User user = new User(userRegistrationDto);
        return userRepository.save(user);
    }

    @Override
    public User addFriend(ContactFormDto contactFormDto) {
        Optional<User> user = userRepository.findByEmail(contactFormDto.getUserEmail());
        Optional<User> friend = userRepository.findByEmail(contactFormDto.getFriendEmail());

        if (user.isPresent() && friend.isPresent()) {
            Contact contact = new Contact(user.get(), friend.get());
            user.get().getRelations().add(contact);
        } else throw new NotFoundException("User not found");
        return userRepository.save(user.get());
    }

    @Override
    public Set<Contact> contacts(String userEmail) {
        return contactRepository.findAllByUserEmail(userEmail);
    }

    @Override
    public Boolean deleteContact(String email) {
        return null;
    }
}

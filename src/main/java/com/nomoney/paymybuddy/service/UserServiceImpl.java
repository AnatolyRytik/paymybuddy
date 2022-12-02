package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import com.nomoney.paymybuddy.model.User;
import com.nomoney.paymybuddy.repository.ContactRepository;
import com.nomoney.paymybuddy.repository.UserRepository;
import com.nomoney.paymybuddy.util.exception.DataAlreadyExistException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<User> existingUser = userRepository.findByEmail(userRegistrationDto.getEmail());
        if (existingUser.isPresent()) {
            throw new DataAlreadyExistException("User with email already exists");
        }
        User user = new User(userRegistrationDto);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

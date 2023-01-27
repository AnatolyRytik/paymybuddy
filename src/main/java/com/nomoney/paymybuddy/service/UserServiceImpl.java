package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import com.nomoney.paymybuddy.model.User;
import com.nomoney.paymybuddy.repository.UserRepository;
import com.nomoney.paymybuddy.util.exception.DataAlreadyExistException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * UserServiceImpl class is an implementation of the UserService interface.
 * It provides methods for saving a user, loading a user by email, and getting user by email.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Saves a user in the database using the data from the UserRegistrationDto object.
     *
     * @param userRegistrationDto The user registration data to be used for creating the user
     * @return The saved user
     * @throws DataAlreadyExistException if a user with the same email already exists in the database
     */
    @Override
    public User saveUser(UserRegistrationDto userRegistrationDto) {
        Optional<User> existingUser = userRepository.findByEmail(userRegistrationDto.getEmail());
        if (existingUser.isPresent()) {
            log.error("User with such email already exists");
            throw new DataAlreadyExistException("User with such email already exists");
        }
        User user = new User(userRegistrationDto);
        log.debug("Saving user with email: {}", userRegistrationDto.getEmail());
        return userRepository.save(user);
    }

    /**
     * Loads a user by their email from the database
     *
     * @param email The email of the user to be loaded
     * @return The UserDetails object representing the user
     * @throws UsernameNotFoundException if the user with the given email could not be found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Bad credentials"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    /**
     * Returns the user with the given email
     *
     * @param email email of the user
     * @return the user
     * @throws NotFoundException if the user with the given email could not be found
     */
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User data not found"));
    }
}

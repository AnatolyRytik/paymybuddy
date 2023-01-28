package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import com.nomoney.paymybuddy.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * The interface for the UserService class.
 * <p>
 * This interface extends the Spring Security UserDetailsService, which provides basic methods for user authentication.
 * <p>
 * It defines additional methods for user management.
 */
public interface UserService extends UserDetailsService {

    /**
     * Method to save a new user to the system.
     *
     * @param userRegistrationDto the dto containing user registration information
     * @return the saved user
     */
    User saveUser(UserRegistrationDto userRegistrationDto);

    /**
     * Method to retrieve a user by email.
     *
     * @param email the email of the user to retrieve
     * @return the user with the given email
     */
    User getUserByEmail(String email);

    /**
     * Method to retrieve a user balance by email.
     *
     * @param email the email of the user to retrieve
     * @return the balance in double value
     */
    Double getUserAccountBalance(String email);
}

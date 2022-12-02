package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import com.nomoney.paymybuddy.model.User;

import java.util.Optional;

public interface UserService {
    User saveUser(UserRegistrationDto userRegistrationDto);

    Optional<User> findUserByEmail(String email);

}

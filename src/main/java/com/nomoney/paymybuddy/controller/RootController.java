package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * RootController class is a Spring MVC controller responsible for handling user login requests.
 */
@Controller
public class RootController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public RootController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Handles GET requests for the "/login" path and returns the login view.
     *
     * @return the login view.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

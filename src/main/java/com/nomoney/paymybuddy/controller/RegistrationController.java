package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import com.nomoney.paymybuddy.service.UserService;
import com.nomoney.paymybuddy.util.exception.DataAlreadyExistException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * This class is responsible for handling registration related requests.
 * <p>
 * It uses UserService and PasswordEncoder to register the user.
 */
@Slf4j
@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        super();
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Handles GET request to /registration and returns registration page
     *
     * @return String registration page
     */
    @GetMapping
    public String showRegistrationForm() {
        log.debug("GET Request to /registration endpoint");
        return "registration";
    }

    /**
     * Returns an instance of UserRegistrationDto
     *
     * @return UserRegistrationDto instance
     */
    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    /**
     * Handles POST request to /registration and register the user
     *
     * @param registrationDto    UserRegistrationDto instance containing registration information
     * @param redirectAttributes RedirectAttributes instance to pass messages to the view
     * @return String redirects to the registration page
     */
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, RedirectAttributes redirectAttributes) {
        log.debug("POST Request to /registration endpoint");
        try {
            registrationDto.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            userService.saveUser(registrationDto);
        } catch (NotFoundException | DataAlreadyExistException e) {
            log.error("Error while registering user: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errors", List.of(e.getMessage()));
        }
        return "redirect:/registration";
    }
}

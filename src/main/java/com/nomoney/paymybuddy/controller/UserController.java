package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.repository.ContactRepository;
import com.nomoney.paymybuddy.service.ContactService;
import com.nomoney.paymybuddy.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;
    private final ContactService contactService;

    public UserController(UserService userService, ContactRepository contactRepository, ContactService contactService) {
        this.userService = userService;
        this.contactService = contactService;
    }

/*    @GetMapping("/allContacts")
    public String getAllContacts(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("contacts", contactService.getContacts(userDetails.getUsername()));
        return "contacts";
    }*/
}
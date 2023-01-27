package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.ContactFormDto;
import com.nomoney.paymybuddy.service.ContactService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contact")
    public String relation(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("contacts", contactService.getContacts(userDetails.getUsername()));
        return "contact";
    }

    @PostMapping("/addConnection")
    public String addFriend(@RequestParam String email,
                            @AuthenticationPrincipal UserDetails userDetails) {
        ContactFormDto contactFormDto = new ContactFormDto(userDetails.getUsername(), email);
        contactService.addFriend(contactFormDto);
        return "redirect:/contact";
    }
/*
    @DeleteMapping("/contact")
    public String deleteContact(@RequestParam Long id) {
        contactService.deleteContact(id);
        return "redirect:/user/contact";
    }*/
}

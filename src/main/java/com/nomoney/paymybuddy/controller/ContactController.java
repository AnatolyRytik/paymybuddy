package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.ContactFormDto;
import com.nomoney.paymybuddy.service.ContactService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
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

    @PostMapping("/addContact")
    public String addFriend(@ModelAttribute("friend") ContactFormDto contactFormDto,
                            @AuthenticationPrincipal UserDetails userDetails) {
        contactFormDto.setUserEmail(userDetails.getUsername());
        contactService.addFriend(contactFormDto);
        return "redirect:/user/contact?success";
    }

    @PostMapping("/contact")
    public String deleteContact(@RequestParam Long id) {
        contactService.deleteContact(id);
        return "redirect:/user/contact";
    }
}

package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.ContactFormDto;
import com.nomoney.paymybuddy.service.ContactService;
import com.nomoney.paymybuddy.util.exception.NotEnoughMoneyException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import com.nomoney.paymybuddy.util.exception.OperationNotAllowedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
                            @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        try {
            ContactFormDto contactFormDto = new ContactFormDto(userDetails.getUsername(), email);
            contactService.addFriend(contactFormDto);
        } catch (NotFoundException | OperationNotAllowedException | NotEnoughMoneyException e) {
            redirectAttributes.addFlashAttribute("errors", List.of(e.getMessage()));
        }
        return "redirect:/contact";
    }
}

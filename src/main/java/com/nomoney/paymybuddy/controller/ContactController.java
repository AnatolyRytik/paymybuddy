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

/**
 * The ContactController class is a Spring MVC controller that handles web requests related to contact management.
 * <p>
 * Constructs an instance of ContactController with the given ContactService.
 *
 * @param contactService the service for retrieving and manipulating contact information
 */
@Controller
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * Handles GET requests to the "/contact" endpoint. Retrieves the contacts for the currently logged in user
     * and adds them to the model.
     *
     * @param model       the model to add the contacts to
     * @param userDetails the UserDetails object for the currently logged in user
     * @return the view name "contact"
     */
    @GetMapping("/contact")
    public String relation(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("contacts", contactService.getContacts(userDetails.getUsername()));
        return "contact";
    }

    /**
     * Handles POST requests to the "/addConnection" endpoint. Attempts to add a friend to the currently logged in user's contacts.
     *
     * @param email              the email address of the friend to add
     * @param userDetails        the UserDetails object for the currently logged in user
     * @param redirectAttributes the RedirectAttributes object to pass flash attributes to the redirect destination
     * @return the view name "redirect:/contact"
     */
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

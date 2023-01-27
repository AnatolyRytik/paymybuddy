package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.InternalTransactionDto;
import com.nomoney.paymybuddy.service.ContactService;
import com.nomoney.paymybuddy.service.TransactionService;
import com.nomoney.paymybuddy.service.UserService;
import com.nomoney.paymybuddy.util.exception.NotEnoughMoneyException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import com.nomoney.paymybuddy.util.exception.OperationNotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * The InternalTransactionController class is a Spring MVC @Controller that handles internal transactions between users.
 * <p>
 * It uses the TransactionService, ContactService and UserService classes to handle logic related to transactions and users.
 */
@Slf4j
@Controller
public class InternalTransactionController {

    private final TransactionService transactionService;
    private final ContactService contactService;
    private final UserService userService;

    public InternalTransactionController(TransactionService transactionService, ContactService contactService, UserService userService) {
        this.transactionService = transactionService;
        this.contactService = contactService;
        this.userService = userService;
    }

    /**
     * Handles GET requests to the "/internalTransaction" endpoint. Retrieves all the internal transactions for the currently logged in user,
     * the contacts for the currently logged in user and the user's balance. Adds them to the model for displaying on the internal transaction page.
     *
     * @param model       the model object to hold the data for the view.
     * @param userDetails an instance of {@link UserDetails} containing the current user's information.
     * @return the name of the internal transaction page.
     */
    @GetMapping("/internalTransaction")
    public String internalTransactionPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("User {} is accessing internalTransactionPage", userDetails.getUsername());
        model.addAttribute("transactions", transactionService.getAllTransactionsByUser(userDetails.getUsername()));
        model.addAttribute("internalTransaction", new InternalTransactionDto());
        model.addAttribute("contacts", contactService.getContacts(userDetails.getUsername()));
        model.addAttribute("balance", userService.getUserByEmail(userDetails.getUsername()).getBalance());

        return "internaltransaction";
    }

    /**
     * Handles POST requests to the "/internalBalanceOperation" endpoint. Creates a new internal transaction using the data from the
     * {@link InternalTransactionDto} object passed in as a parameter.
     *
     * @param internalTransactionDto an instance of {@link InternalTransactionDto} containing the data for the new transaction.
     * @param userDetails            an instance of {@link UserDetails} containing the current user's information.
     * @param redirectAttributes     an instance of {@link RedirectAttributes} used to store the error messages in case of any exception.
     * @return redirect to the internalTransaction page.
     */
    @PostMapping("/internalBalanceOperation")
    public String createInternalTransaction(@ModelAttribute InternalTransactionDto internalTransactionDto, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        log.debug("User {} is attempting to make an internal transaction", userDetails.getUsername());
        try {
            internalTransactionDto.setUserEmail(userDetails.getUsername());
            transactionService.createInternalTransaction(internalTransactionDto);
        } catch (NotFoundException | OperationNotAllowedException | NotEnoughMoneyException e) {
            log.error("An error occurred while trying to perform an internal transaction: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errors", List.of(e.getMessage()));
        }
        return "redirect:/internalTransaction";
    }
}

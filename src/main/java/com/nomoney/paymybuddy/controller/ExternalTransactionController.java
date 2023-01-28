package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.ExternalTransactionDto;
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
 * The ExternalTransactionController class is responsible for handling requests related to external transactions.
 * <p>
 * It uses the TransactionService and UserService classes to handle logic related to transactions and users.
 */
@Slf4j
@Controller
public class ExternalTransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    public ExternalTransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    /**
     * Handles GET requests to the "/externalTransaction" endpoint. Retrieves all transactions made by the logged in user,
     * their balance and adds them to the model.
     *
     * @param model       The model object to add attributes to.
     * @param userDetails The currently logged in user.
     * @return The name of the template to be rendered.
     */
    @GetMapping("/externalTransaction")
    public String externalTransactionPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("User {} is accessing externalTransactionPage", userDetails.getUsername());
        model.addAttribute("transactions", transactionService.getAllTransactionsByUser(userDetails.getUsername()));
        model.addAttribute("externalTransaction", new ExternalTransactionDto());
        model.addAttribute("user", userDetails.getUsername());
        model.addAttribute("balance", userService.getUserAccountBalance(userDetails.getUsername()));
        return "externaltransaction";
    }


    /**
     * Handles POST requests to the "/externalBalanceOperation" endpoint. Add money to or withdraw money from bank account.
     *
     * @param externalTransactionDto An instance of the ExternalTransactionDto class that contains the information
     * @param userDetails            The currently logged in user.
     * @param redirectAttributes     An object that can be used to pass attributes to the response.
     * @return A redirect to the "/externalTransaction" endpoint.
     */
    @PostMapping("/externalBalanceOperation")
    public String addFromOrWithdrawToBankAccount(@ModelAttribute ExternalTransactionDto externalTransactionDto, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        log.debug("User {} is attempting to make an external transaction", userDetails.getUsername());
        try {
            externalTransactionDto.setUserEmail(userDetails.getUsername());
            transactionService.setMoneyAvailable(externalTransactionDto);
        } catch (NotFoundException | OperationNotAllowedException | NotEnoughMoneyException e) {
            log.error("An error occurred while trying to perform an external transaction: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errors", List.of(e.getMessage()));
        }
        return "redirect:/externalTransaction";
    }
}

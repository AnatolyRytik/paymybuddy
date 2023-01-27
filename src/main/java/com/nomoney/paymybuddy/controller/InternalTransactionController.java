package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.InternalTransactionDto;
import com.nomoney.paymybuddy.service.ContactService;
import com.nomoney.paymybuddy.service.TransactionService;
import com.nomoney.paymybuddy.service.UserService;
import com.nomoney.paymybuddy.util.exception.NotEnoughMoneyException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

    @GetMapping("/internalTransaction")
    public String internalTransactionPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("transactions", transactionService.getAllTransactionsByUser(userDetails.getUsername()));
        model.addAttribute("internalTransaction", new InternalTransactionDto());
        model.addAttribute("contacts", contactService.getContacts(userDetails.getUsername()));
        model.addAttribute("balance", userService.getUserByEmail(userDetails.getUsername()).getBalance());

        return "internaltransaction";
    }

    @PostMapping("/internalBalanceOperation")
    public String createInternalTransaction(@ModelAttribute InternalTransactionDto internalTransactionDto, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        internalTransactionDto.setUserEmail(userDetails.getUsername());
        try {
            transactionService.createInternalTransaction(internalTransactionDto);
        } catch (NotFoundException | NotEnoughMoneyException e) {
            redirectAttributes.addFlashAttribute("errors", List.of(e.getMessage()));
        }
        return "redirect:/internalTransaction";
    }
}

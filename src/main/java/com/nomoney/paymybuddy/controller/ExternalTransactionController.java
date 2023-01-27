package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.ExternalTransactionDto;
import com.nomoney.paymybuddy.service.TransactionService;
import com.nomoney.paymybuddy.service.UserService;
import com.nomoney.paymybuddy.util.exception.NotEnoughMoneyException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import com.nomoney.paymybuddy.util.exception.OperationNotAllowedException;
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
public class ExternalTransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    public ExternalTransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping("/externalTransaction")
    public String externalTransactionPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("transactions", transactionService.getAllTransactionsByUser(userDetails.getUsername()));
        model.addAttribute("externalTransaction", new ExternalTransactionDto());
        model.addAttribute("user", userDetails.getUsername());
        model.addAttribute("balance", userService.getUserByEmail(userDetails.getUsername()).getBalance());
        return "externaltransaction";
    }

    @PostMapping("/externalBalanceOperation")
    public String addFromOrWithdrawToBankAccount(@ModelAttribute ExternalTransactionDto externalTransactionDto, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        try {
            externalTransactionDto.setUserEmail(userDetails.getUsername());
            transactionService.setMoneyAvailable(externalTransactionDto);
        } catch (NotFoundException | OperationNotAllowedException | NotEnoughMoneyException e) {
            redirectAttributes.addFlashAttribute("errors", List.of(e.getMessage()));
        }
        return "redirect:/externalTransaction";
    }
}

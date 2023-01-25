package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.ExternalTransactionDto;
import com.nomoney.paymybuddy.service.TransactionService;
import com.nomoney.paymybuddy.util.exception.NotEnoughMoneyException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
public class ExternalTransactionController {

    private final TransactionService transactionService;

    public ExternalTransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/externalTransaction")
    public String externalTransactionPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("transactions", transactionService.getAllTransactionsByUser(userDetails.getUsername()));
        model.addAttribute("externalTransaction", new ExternalTransactionDto());
        model.addAttribute("user", userDetails.getUsername());
        return "externalTransaction";
    }

    @PostMapping("/externalTransaction/balanceOperation")
    public String withdrawToBankAccount(@ModelAttribute ExternalTransactionDto externalTransactionDto, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        try {
            externalTransactionDto.setUserEmail(userDetails.getUsername());
            transactionService.setMoneyAvailable(externalTransactionDto);
        } catch (NotFoundException | NotEnoughMoneyException e) {
            redirectAttributes.addFlashAttribute("errors", List.of(e.getMessage()));
        }
        return "redirect:/user/externalTransaction";

    }
}

package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.ExternalTransactionDto;
import com.nomoney.paymybuddy.service.TransactionService;
import com.nomoney.paymybuddy.util.exception.NotEnoughMoneyException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

        return "externalTransaction";
    }

    @PostMapping("/externalTransaction/withdrawal")
    public String withdrawToBankAccount(@ModelAttribute ExternalTransactionDto externalTransactionDto, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        try {
            externalTransactionDto.setUserEmail(userDetails.getUsername());
            transactionService.withdrawToBankAccount(externalTransactionDto);
        } catch (NotFoundException | NotEnoughMoneyException e) {
            redirectAttributes.addFlashAttribute("errors", List.of(e.getMessage()));
        }
        return "redirect:/user/externalTransaction";

    }
}

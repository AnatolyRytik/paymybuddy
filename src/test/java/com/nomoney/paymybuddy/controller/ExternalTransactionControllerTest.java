package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.ExternalTransactionDto;
import com.nomoney.paymybuddy.model.Transaction;
import com.nomoney.paymybuddy.service.TransactionService;
import com.nomoney.paymybuddy.service.UserService;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExternalTransactionController.class)
public class ExternalTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void getTransactionsTest() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        when(transactionService.getAllTransactionsByUser("user@example.com")).thenReturn(transactions);
        mockMvc.perform(get("/externalTransaction"))
                .andExpect(status().isOk())
                .andExpect(view().name("externaltransaction"))
                .andExpect(model().attributeExists("transactions"));
    }

    @Test
    @WithMockUser
    void externalTransaction_shouldReturnTemplateWithExpectedAttributes() throws Exception {
        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        externalTransactionDto.setAmountToAdd(100.0);
        externalTransactionDto.setUserIban("JDKJKQS39A0");
        externalTransactionDto.setUserEmail("user@example.com");
        externalTransactionDto.setDescription("taxi");

        mockMvc.perform(post("/externalBalanceOperation")
                        .with(csrf())
                        .sessionAttr("externalTransactionDto", externalTransactionDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/externalTransaction"));
    }

    @Test
    @WithMockUser
    void externalTransaction_shouldReturnError() throws Exception {
        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        externalTransactionDto.setAmountToAdd(100.0);
        externalTransactionDto.setUserIban("JDKJKQS39A0");
        externalTransactionDto.setUserEmail("user@example.com");
        externalTransactionDto.setDescription("taxi");
        doThrow(new NotFoundException("Error message")).when(transactionService).setMoneyAvailable(any());
        mockMvc.perform(post("/externalBalanceOperation")
                        .with(csrf())
                        .sessionAttr("externalTransactionDto", externalTransactionDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/externalTransaction"))
                .andExpect(flash().attributeExists("errors"));
    }
}

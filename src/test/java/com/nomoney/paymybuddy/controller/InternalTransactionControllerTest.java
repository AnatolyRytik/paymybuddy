package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.InternalTransactionDto;
import com.nomoney.paymybuddy.model.Transaction;
import com.nomoney.paymybuddy.service.ContactService;
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

@WebMvcTest(InternalTransactionController.class)
class InternalTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private UserService userService;

    @MockBean
    private ContactService contactService;

    @Test
    @WithMockUser
    void getTransactionsTest() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        when(transactionService.getAllTransactionsByUser("user@example.com")).thenReturn(transactions);
        mockMvc.perform(get("/internalTransaction"))
                .andExpect(status().isOk())
                .andExpect(view().name("internaltransaction"))
                .andExpect(model().attributeExists("transactions"));
    }

    @Test
    @WithMockUser
    void externalTransaction_shouldReturnTemplateWithExpectedAttributes() throws Exception {
        InternalTransactionDto internalTransactionDto = new InternalTransactionDto();
        internalTransactionDto.setAmount(100.0);
        internalTransactionDto.setDescription("taxi");
        internalTransactionDto.setEmailRecipient("emailrecipient@example.com");
        internalTransactionDto.setUserEmail("user@example.com");

        mockMvc.perform(post("/internalBalanceOperation")
                        .with(csrf())
                        .sessionAttr("internalTransactionDto", internalTransactionDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/internalTransaction"));
    }

    @Test
    @WithMockUser
    void externalTransaction_shouldReturnError() throws Exception {
        InternalTransactionDto internalTransactionDto = new InternalTransactionDto();
        internalTransactionDto.setAmount(100.0);
        internalTransactionDto.setDescription("taxi");
        internalTransactionDto.setEmailRecipient("emailrecipient@example.com");
        internalTransactionDto.setUserEmail("user@example.com");
        doThrow(new NotFoundException("Error message")).when(transactionService).createInternalTransaction(any());
        mockMvc.perform(post("/internalBalanceOperation")
                        .with(csrf())
                        .sessionAttr("externalTransactionDto", internalTransactionDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/internalTransaction"))
                .andExpect(flash().attributeExists("errors"));
    }
}
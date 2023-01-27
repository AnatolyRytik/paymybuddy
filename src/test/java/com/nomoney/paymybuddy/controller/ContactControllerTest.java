package com.nomoney.paymybuddy.controller;


import com.nomoney.paymybuddy.service.ContactService;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Test
    @WithMockUser
    void relation() throws Exception {
        when(contactService.getContacts("username")).thenReturn(List.of());
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("contacts"));
    }

    @Test
    @WithMockUser
    void addFriendSuccess() throws Exception {
        mockMvc.perform(post("/addConnection").with(csrf()).param("email", "user@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/contact"));
    }

    @Test
    @WithMockUser
    void addFriendFailure() throws Exception {
        doThrow(new NotFoundException("Error message")).when(contactService).addFriend(any());
        mockMvc.perform(post("/addConnection").with(csrf()).param("email", "user@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/contact"))
                .andExpect(flash().attributeExists("errors"));
    }
}
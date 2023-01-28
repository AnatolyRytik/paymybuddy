package com.nomoney.paymybuddy.controller;

import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import com.nomoney.paymybuddy.model.User;
import com.nomoney.paymybuddy.service.UserService;
import com.nomoney.paymybuddy.util.exception.DataAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser
    void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    @WithMockUser
    void testRegisterUserAccount() throws Exception {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("User", "Example", "testpassword", "testuse@mail.com");
        when(userService.saveUser(userRegistrationDto)).thenReturn(new User("User", "Example", "testuse@mail.com", "testpassword", 100.0, new Date()));
        when(passwordEncoder.encode("testpassword")).thenReturn("encodedpassword");

        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .flashAttr("user", userRegistrationDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/registration"));
    }

    @Test
    @WithMockUser
    void testRegisterUserAccountWithError() throws Exception {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("testuser@mail.com");
        userRegistrationDto.setPassword("testpassword");

        doThrow(DataAlreadyExistException.class).when(userService).saveUser(userRegistrationDto);
        when(passwordEncoder.encode("testpassword")).thenReturn("encodedpassword");

        mockMvc.perform(post("/registration")
                        .flashAttr("user", userRegistrationDto))
                .andExpect(status().is4xxClientError());
    }
}

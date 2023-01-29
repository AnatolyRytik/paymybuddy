package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import com.nomoney.paymybuddy.model.User;
import com.nomoney.paymybuddy.repository.UserRepository;
import com.nomoney.paymybuddy.util.exception.DataAlreadyExistException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;


    @Test
    void saveUser_whenUserAlreadyExists_ShouldThrowDataAlreadyExistException() {
        //GIVEN
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto("User", "Example", "password", "testuser@mail.com");
        User user = new User(userRegistrationDto);

        //WHEN
        when(userRepository.findByEmail(userRegistrationDto.getEmail())).thenReturn(Optional.of(user));

        //THEN
        assertThatThrownBy(() -> userServiceImpl.saveUser(userRegistrationDto))
                .isInstanceOf(DataAlreadyExistException.class)
                .hasMessage("User with such email already exists");

        verify(userRepository).findByEmail(userRegistrationDto.getEmail());
        verify(userRepository, never()).save(user);
    }

    @Test
    public void getUserByEmail_whenUserExists_ShouldReturnUser() {
        //GIVEN
        User expectedUser = new User("User", "Example", "test@mail.com", "password", 0.0, new Date());

        //WHEN
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(expectedUser));
        User result = userServiceImpl.getUserByEmail("test@mail.com");

        //THEN
        assertEquals(expectedUser, result);
        verify(userRepository, times(1)).findByEmail("test@mail.com");
    }

    @Test
    public void getUserByEmail_whenUserDoesNotExist_ShouldThrowNotFoundException() {

        //WHEN
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userServiceImpl.getUserByEmail("test@mail.com"));

        //THEN
        assertEquals("User data not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@mail.com");
    }

    @Test
    public void getUserAccountBalance_whenUserExists_ShouldReturnCorrectBalance() {
        //GIVEN
        User user = new User();
        user.setEmail("test@mail.com");
        user.setBalance(100.0);

        //WHEN
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        Double result = userService.getUserAccountBalance("test@mail.com");

        //THEN
        assertThat(result).isEqualTo(100.0);
        verify(userRepository, times(1)).findByEmail("test@mail.com");
    }

    @Test
    public void getUserAccountBalance_whenUserDoesNotExist_ShouldThrowNotFoundException() {
        //WHEN
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        UserServiceImpl userService = new UserServiceImpl(userRepository);

        //THEN
        assertThatThrownBy(() -> userService.getUserAccountBalance("test@mail.com"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User data not found");
        verify(userRepository, times(1)).findByEmail("test@mail.com");
    }
}
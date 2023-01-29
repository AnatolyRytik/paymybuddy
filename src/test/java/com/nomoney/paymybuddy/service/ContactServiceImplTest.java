package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.ContactFormDto;
import com.nomoney.paymybuddy.model.Contact;
import com.nomoney.paymybuddy.model.User;
import com.nomoney.paymybuddy.repository.ContactRepository;
import com.nomoney.paymybuddy.repository.UserRepository;
import com.nomoney.paymybuddy.util.exception.DataAlreadyExistException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    void testAddFriend_Success() {
        // GIVEN
        ContactFormDto contactFormDto = new ContactFormDto("testuser@mail.com", "testfriend@mail.com");
        User user = new User("User", "Example", "testuser@mail.com", "testpassword", 100.0, new Date());
        User friend = new User("Friend", "Example", "testfriend@mail.com", "testpassword", 100.0, new Date());
        Contact contact = new Contact(user, friend);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);

        // WHEN
        when(userRepository.findByEmail(contactFormDto.getUserEmail())).thenReturn(Optional.of(user));
        when(contactRepository.findAllByUserEmail(user.getEmail())).thenReturn(contacts);
        Contact result = contactService.getContacts(user.getEmail()).get(0);

        // THEN
        assertEquals(contact, result);
    }

    @Test
    void testAddFriend_DataAlreadyExistException() {
        // GIVEN
        ContactFormDto contactFormDto = new ContactFormDto("testuser2@mail.com", "testfriend2@mail.com");
        User user = new User("User2", "Example", "testuser2@mail.com", "testpassword", 100.0, new Date());
        User friend = new User("Friend2", "Example", "testfriend2@mail.com", "testpassword", 100.0, new Date());
        Contact contact = new Contact(user, friend);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);

        // WHEN
        when(userRepository.findByEmail(contactFormDto.getUserEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(contactFormDto.getFriendEmail())).thenReturn(Optional.of(friend));
        when(contactRepository.findAllByUserEmail(user.getEmail())).thenReturn(contacts);

        // THEN
        assertThrows(DataAlreadyExistException.class, () -> contactService.addFriend(contactFormDto));
        verify(userRepository, times(2)).findByEmail(anyString());
        verify(contactRepository, times(1)).findAllByUserEmail(user.getEmail());
    }

    @Test
    void testAddFriend_NotFoundException() {
        // GIVEN
        ContactFormDto contactFormDto = new ContactFormDto("user@example.com", "friend@example.com");

        // WHEN
        when(userRepository.findByEmail(contactFormDto.getUserEmail())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(contactFormDto.getFriendEmail())).thenReturn(Optional.empty());

        // THEN
        assertThrows(NotFoundException.class, () -> contactService.addFriend(contactFormDto));
    }

    @Test
    public void testGetContacts_Success() {
        // GIVEN
        String userEmail = "testuser3@mail.com";
        User user = new User("User3", "Example", userEmail, "testpassword", 100.0, new Date());
        User friend1 = new User("Friend3", "Example", "testfriend3@mail.com", "testpassword", 100.0, new Date());
        User friend2 = new User("Friend4", "Example", "testfriend4@mail.com", "testpassword", 100.0, new Date());
        Contact contact1 = new Contact(user, friend1);
        Contact contact2 = new Contact(user, friend2);
        List<Contact> expectedContacts = new ArrayList<>();
        expectedContacts.add(contact1);
        expectedContacts.add(contact2);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(contactRepository.findAllByUserEmail(userEmail)).thenReturn(expectedContacts);

        // WHEN
        List<Contact> contacts = contactService.getContacts(userEmail);

        // THEN
        assertThat(contacts).isEqualTo(expectedContacts);
    }

    @Test
    void testGetContacts_NotFoundException() {
        // GIVEN
        String userEmail = "non-existing@email.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // WHEN-THEN
        assertThrows(NotFoundException.class, () -> contactService.getContacts(userEmail));
    }

    @Test
    void testGetContacts_NoContacts() {
        // GIVEN
        String userEmail = "existing@email.com";
        User user = new User("User3", "Example", userEmail, "testpassword", 100.0, new Date());
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(contactRepository.findAllByUserEmail(userEmail)).thenReturn(Collections.emptyList());

        // WHEN
        List<Contact> contacts = contactService.getContacts(userEmail);

        // THEN
        assertTrue(contacts.isEmpty());
    }
}

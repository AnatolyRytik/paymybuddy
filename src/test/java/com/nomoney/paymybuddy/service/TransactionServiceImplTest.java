package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.ExternalTransactionDto;
import com.nomoney.paymybuddy.dto.InternalTransactionDto;
import com.nomoney.paymybuddy.model.Transaction;
import com.nomoney.paymybuddy.model.User;
import com.nomoney.paymybuddy.repository.TransactionRepository;
import com.nomoney.paymybuddy.repository.UserRepository;
import com.nomoney.paymybuddy.util.exception.NotEnoughMoneyException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.nomoney.paymybuddy.constant.Constant.FEE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void createInternalTransaction_shouldReturnTransaction_whenUserAndRecipientAreFound() {
        //GIVEN
        InternalTransactionDto internalTransactionDto = new InternalTransactionDto();
        User user = new User();
        User recipient = new User();

        //WHEN
        when(userRepository.findByEmail(internalTransactionDto.getUserEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(internalTransactionDto.getEmailRecipient())).thenReturn(Optional.of(recipient));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        Transaction result = transactionService.createInternalTransaction(internalTransactionDto);

        //THEN
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        assertNotNull(result);
    }

    @Test
    void createInternalTransaction_shouldThrowNotFoundException_whenUserIsNotFound() {
        //GIVEN
        InternalTransactionDto internalTransactionDto = new InternalTransactionDto();

        //WHEN
        when(userRepository.findByEmail(internalTransactionDto.getUserEmail())).thenReturn(Optional.empty());

        //THEN
        assertThrows(NotFoundException.class, () -> transactionService.createInternalTransaction(internalTransactionDto));
    }

    @Test
    void createInternalTransaction_shouldThrowNotFoundException_whenRecipientIsNotFound() {
        //GIVEN
        InternalTransactionDto internalTransactionDto = new InternalTransactionDto();
        User user = new User();

        //WHEN
        when(userRepository.findByEmail(internalTransactionDto.getUserEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(internalTransactionDto.getEmailRecipient())).thenReturn(Optional.empty());

        //THEN
        assertThrows(NotFoundException.class, () -> transactionService.createInternalTransaction(internalTransactionDto));
    }

    @Test
    void createInternalTransaction_shouldThrowNotEnoughMoneyException_whenUserDoesNotHaveEnoughMoney() {
        //GIVEN
        InternalTransactionDto internalTransactionDto = new InternalTransactionDto();
        User user = new User();
        User recipient = new User();

        user.setBalance(10.0);
        internalTransactionDto.setAmount(20.0);

        //WHEN
        when(userRepository.findByEmail(internalTransactionDto.getUserEmail())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(internalTransactionDto.getEmailRecipient())).thenReturn(Optional.of(recipient));

        //THEN
        assertThrows(NotEnoughMoneyException.class, () -> {
            transactionService.createInternalTransaction(internalTransactionDto);
        });
    }

    @Test
    public void testFeeCalculation() {
        //GIVEN
        InternalTransactionDto internalTransactionDto = new InternalTransactionDto();
        internalTransactionDto.setAmount(100.0);
        internalTransactionDto.setUserEmail("test@test.com");
        internalTransactionDto.setEmailRecipient("recipient@test.com");

        //WHEN
        User user = new User();
        user.setEmail("test@test.com");
        user.setBalance(1000.0);
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        User recipient = new User();
        recipient.setEmail("recipient@test.com");
        recipient.setBalance(0.0);
        when(userRepository.findByEmail("recipient@test.com")).thenReturn(Optional.of(recipient));

        transactionService.createInternalTransaction(internalTransactionDto);

        double expectedUserBalance = 1000.0 - 100.0 - (100.0 * FEE);
        double expectedRecipientBalance = 100.0;

        //THEN
        assertEquals(expectedUserBalance, user.getBalance(), 0.001);
        assertEquals(expectedRecipientBalance, recipient.getBalance(), 0.001);
    }

    @Test
    public void testSetMoneyAvailable_AddMoney_Success() throws Exception {
        //GIVEN
        User user = new User("User", "Example", "testuser@mail.com", "testpassword", 100.0, new Date());

        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        externalTransactionDto.setUserEmail("email@example.com");
        externalTransactionDto.setAmountToAdd(50.0);
        externalTransactionDto.setAmountToWithdraw(0.0);

        //WHEN
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.of(user));
        Transaction transaction = transactionService.setMoneyAvailable(externalTransactionDto);

        //THEN
        assertEquals(150.0, user.getBalance(), 0.0);
    }

    @Test
    public void testSetMoneyAvailable_AddMoney_UserNotFound() throws Exception {
        //GIVEN
        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        externalTransactionDto.setUserEmail("email@example.com");
        externalTransactionDto.setAmountToAdd(50.0);
        externalTransactionDto.setAmountToWithdraw(0.0);

        //WHEN
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.empty());

        //THEN
        assertThrows(NotFoundException.class, () -> {
            transactionService.setMoneyAvailable(externalTransactionDto);
        });
    }

    @Test
    public void testSetMoneyAvailable_WithdrawMoney_Success() throws Exception {
        //GIVEN
        User user = new User("User", "Example", "testuser@mail.com", "testpassword", 100.0, new Date());

        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        externalTransactionDto.setUserEmail("email@example.com");
        externalTransactionDto.setAmountToAdd(0.0);
        externalTransactionDto.setAmountToWithdraw(50.0);

        //WHEN
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.of(user));
        Transaction transaction = transactionService.setMoneyAvailable(externalTransactionDto);

        //THEN
        assertEquals(50.0, user.getBalance(), 0.0);
    }

    @Test
    public void testSetMoneyAvailable_WithdrawMoney_UserNotFound() throws Exception {
        //GIVEN
        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        externalTransactionDto.setUserEmail("email@example.com");
        externalTransactionDto.setAmountToAdd(0.0);
        externalTransactionDto.setAmountToWithdraw(50.0);

        //WHEN
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.empty());

        //THEN
        assertThrows(NotFoundException.class, () -> {
            transactionService.setMoneyAvailable(externalTransactionDto);
            ;
        });
    }

    @Test
    public void testGetAllTransactionsByUser() {
        // GIVEN
        String email = "user@example.com";
        InternalTransactionDto internalTransactionDto = new InternalTransactionDto();
        internalTransactionDto.setAmount(100.0);
        internalTransactionDto.setUserEmail(email);
        internalTransactionDto.setEmailRecipient("recipient@test.com");

        InternalTransactionDto internalTransactionDto2 = new InternalTransactionDto();
        internalTransactionDto.setAmount(100.0);
        internalTransactionDto.setUserEmail(email);
        internalTransactionDto.setEmailRecipient("recipient2@test.com");
        Transaction transaction1 = new Transaction(internalTransactionDto);
        Transaction transaction2 = new Transaction(internalTransactionDto2);
        List<Transaction> expectedTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findAllByUserEmail(email)).thenReturn(expectedTransactions);

        // WHEN
        List<Transaction> actualTransactions = transactionService.getAllTransactionsByUser(email);

        // THEN
        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionRepository).findAllByUserEmail(email);
    }
}

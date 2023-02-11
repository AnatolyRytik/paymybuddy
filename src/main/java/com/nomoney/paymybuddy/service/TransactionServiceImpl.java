package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.ExternalTransactionDto;
import com.nomoney.paymybuddy.dto.InternalTransactionDto;
import com.nomoney.paymybuddy.model.Transaction;
import com.nomoney.paymybuddy.model.User;
import com.nomoney.paymybuddy.repository.TransactionRepository;
import com.nomoney.paymybuddy.repository.UserRepository;
import com.nomoney.paymybuddy.util.exception.NotEnoughMoneyException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import com.nomoney.paymybuddy.util.exception.OperationNotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nomoney.paymybuddy.constant.Constant.FEE;

/**
 * This class is the implementation of the {@link TransactionService} interface.
 * The class uses {@link TransactionRepository} and {@link UserRepository} to
 * interact with the database and perform operations related to transactions.
 */
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates an internal transaction between two users.
     *
     * @param internalTransactionDto {@link InternalTransactionDto} containing details of the transaction.
     * @return {@link Transaction} object representing the created transaction.
     * @throws NotFoundException       if user or recipient account is
     *                                 not found.
     * @throws NotEnoughMoneyException if the user does not have enough money to complete the transaction.
     */
    @Transactional
    public Transaction createInternalTransaction(InternalTransactionDto internalTransactionDto) {
        double amount = internalTransactionDto.getAmount();

        User user = userRepository.findByEmail(internalTransactionDto.getUserEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        User recipient = userRepository.findByEmail(internalTransactionDto.getEmailRecipient())
                .orElseThrow(() -> new NotFoundException("Recipient account not found"));

        Transaction transaction = new Transaction(internalTransactionDto);

        if (user.getBalance() >= amount + (amount * FEE)) {
            user.setBalance(user.getBalance() - amount - (amount * FEE));
            recipient.setBalance(recipient.getBalance() + amount);
            userRepository.save(user);
            userRepository.save(recipient);
            log.debug("Transaction created: {}", transaction);
            return transactionRepository.save(transaction);
        } else {
            log.error("Not enough money");
            throw new NotEnoughMoneyException("Not enough money");
        }
    }

    /**
     * Add or withdraw money from a user's account.
     *
     * @param externalTransactionDto {@link ExternalTransactionDto} containing details of the transaction.
     * @return {@link Transaction} object representing the created transaction.
     * @throws NotFoundException            if user is not found.
     * @throws OperationNotAllowedException if both amountToAdd and amountToWithdraw are greater than 0.
     * @throws NotEnoughMoneyException      if user does not have enough money to complete the withdraw transaction.
     */
    @Override
    @Transactional
    public Transaction setMoneyAvailable(ExternalTransactionDto externalTransactionDto) {
        User user = userRepository.findByEmail(externalTransactionDto.getUserEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Transaction transaction;

        if (externalTransactionDto.getAmountToAdd() > 0.0 && externalTransactionDto.getAmountToWithdraw() == 0.0) {
            transaction = new Transaction(externalTransactionDto);
            return addMoney(user, externalTransactionDto.getAmountToAdd(), transaction);
        } else if (externalTransactionDto.getAmountToWithdraw() > 0.0 && externalTransactionDto.getAmountToAdd() == 0.0) {
            transaction = new Transaction(externalTransactionDto);
            return withdrawMoney(user, externalTransactionDto.getAmountToWithdraw(), transaction);
        } else {
            log.error("This operation is not allowed");
            throw new OperationNotAllowedException("This operation is not allowed");
        }
    }

    /**
     * Withdraws money from a user's account.
     *
     * @param user        {@link User} object representing the user.
     * @param amount      amount of money to withdraw.
     * @param transaction {@link Transaction} object representing the created transaction.
     * @return {@link Transaction} object representing the created transaction.
     * @throws NotEnoughMoneyException if user does not have enough money to complete the withdraw transaction.
     */
    private Transaction withdrawMoney(User user, double amount, Transaction transaction) {
        if (user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            userRepository.save(user);
            return transactionRepository.save(transaction);
        } else {
            log.error("Not enough money");
            throw new NotEnoughMoneyException("Not enough money");
        }
    }

    /**
     * Method to add money to a user's account and create a corresponding transaction.
     *
     * @param user        the user whose account needs to be updated
     * @param amount      the amount of money to be added
     * @param transaction the transaction object to be saved
     * @return the saved transaction
     */
    private Transaction addMoney(User user, double amount, Transaction transaction) {
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        log.debug("Transaction created: {}", transaction);
        return transactionRepository.save(transaction);
    }

    /**
     * Method to retrieve all transactions of a user by email.
     *
     * @param email the email of the user
     * @return a list of all transactions of the user
     */
    public List<Transaction> getAllTransactionsByUser(String email) {
        return this.transactionRepository.findAllByUserEmail(email);
    }
}

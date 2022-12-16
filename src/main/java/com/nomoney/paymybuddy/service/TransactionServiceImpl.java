package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.model.Transaction;
import com.nomoney.paymybuddy.model.User;
import com.nomoney.paymybuddy.repository.AccountRepository;
import com.nomoney.paymybuddy.repository.TransactionRepository;
import com.nomoney.paymybuddy.repository.UserRepository;
import com.nomoney.paymybuddy.util.exception.NotEnoughMoneyException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Transaction createInternalTransaction(Transaction transaction) {
        double amount = transaction.getAmount();

        User user = userRepository.findByEmail(transaction.getUserEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        User recipient = userRepository.findByEmail(transaction.getEmailRecipient())
                .orElseThrow(() -> new NotFoundException("Recipient account not found"));

        if (user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            recipient.setBalance(recipient.getBalance() + amount);
            userRepository.save(user);
            userRepository.save(recipient);
            return transactionRepository.save(transaction);
        } else throw new NotEnoughMoneyException("Not enough money");
    }


    public List<Transaction> getAllTransactionsByUser(String email) {
        return this.transactionRepository.findAllByUserEmail(email);
    }
}

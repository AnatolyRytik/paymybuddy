package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.model.Account;
import com.nomoney.paymybuddy.model.Transaction;
import com.nomoney.paymybuddy.repository.AccountRepository;
import com.nomoney.paymybuddy.repository.TransactionRepository;
import com.nomoney.paymybuddy.util.exception.NotEnoughMoneyException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Transaction create(Transaction transaction) {
        double amount = transaction.getAmount();

        Account userAccount = this.accountRepository.findByEmail(transaction.getUserEmail())
                .orElseThrow(() -> new NotFoundException("User account not found"));

        Account recipientAccount = this.accountRepository.findByEmail(transaction.getEmailRecipient())
                .orElseThrow(() -> new NotFoundException("Recipient account not found"));

        if (userAccount.getBalance() >= amount) {
            userAccount.setBalance(userAccount.getBalance() - amount);
            recipientAccount.setBalance(recipientAccount.getBalance() + amount);
            this.accountRepository.save(userAccount);
            this.accountRepository.save(recipientAccount);

            return this.transactionRepository.save(transaction);
        } else throw new NotEnoughMoneyException("Not enough money");
    }


    public List<Transaction> getAllTransactionsByUser(String email) {
        return this.transactionRepository.findAllByUserEmail(email);
    }
}

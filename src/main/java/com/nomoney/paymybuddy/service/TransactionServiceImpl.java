package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.ExternalTransactionDto;
import com.nomoney.paymybuddy.dto.InternalTransactionDto;
import com.nomoney.paymybuddy.model.Transaction;
import com.nomoney.paymybuddy.model.User;
import com.nomoney.paymybuddy.repository.AccountRepository;
import com.nomoney.paymybuddy.repository.TransactionRepository;
import com.nomoney.paymybuddy.repository.UserRepository;
import com.nomoney.paymybuddy.util.exception.NotEnoughMoneyException;
import com.nomoney.paymybuddy.util.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nomoney.paymybuddy.constant.Constant.FEE;

@Service
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
    public Transaction createInternalTransaction(InternalTransactionDto internalTransactionDto) {
        double amount = internalTransactionDto.getAmount();

        User user = userRepository.findByEmail(internalTransactionDto.getUserEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        User recipient = userRepository.findByEmail(internalTransactionDto.getEmailRecipient())
                .orElseThrow(() -> new NotFoundException("Recipient account not found"));

        Transaction transaction = new Transaction(internalTransactionDto);

        if (user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - (amount * FEE));
            recipient.setBalance(recipient.getBalance() + amount);
            userRepository.save(user);
            userRepository.save(recipient);
            return transactionRepository.save(transaction);
        } else throw new NotEnoughMoneyException("Not enough money");
    }

    @Override
    public Transaction popUpBalance(ExternalTransactionDto externalTransactionDto) {
        double amount = externalTransactionDto.getAmount();

        User user = userRepository.findByEmail(externalTransactionDto.getUserEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Transaction transaction = new Transaction(externalTransactionDto);

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        return transactionRepository.save(transaction);

    }

    @Override
    public Transaction withdrawToBankAccount(ExternalTransactionDto externalTransactionDto) {
        double amount = externalTransactionDto.getAmount();

        User user = userRepository.findByEmail(externalTransactionDto.getUserEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Transaction transaction = new Transaction(externalTransactionDto);

        if (user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            userRepository.save(user);
            return transactionRepository.save(transaction);
        } else throw new NotEnoughMoneyException("Not enough money");
    }


    public List<Transaction> getAllTransactionsByUser(String email) {
        return this.transactionRepository.findAllByUserEmail(email);
    }
}

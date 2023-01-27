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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.nomoney.paymybuddy.constant.Constant.FEE;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
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
            user.setBalance(user.getBalance() - amount - (amount * FEE));
            recipient.setBalance(recipient.getBalance() + amount);
            userRepository.save(user);
            userRepository.save(recipient);
            return transactionRepository.save(transaction);
        } else throw new NotEnoughMoneyException("Not enough money");
    }

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
        } else throw new OperationNotAllowedException("This operation is not allowed");
    }

    private Transaction withdrawMoney(User user, double amount, Transaction transaction) {
        if (user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            userRepository.save(user);
            return transactionRepository.save(transaction);
        } else throw new NotEnoughMoneyException("Not enough money");
    }

    private Transaction addMoney(User user, double amount, Transaction transaction) {
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
        return transactionRepository.save(transaction);
    }


    public List<Transaction> getAllTransactionsByUser(String email) {
        return this.transactionRepository.findAllByUserEmail(email);
    }
}

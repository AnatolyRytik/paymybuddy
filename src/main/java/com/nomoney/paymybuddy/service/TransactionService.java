package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.ExternalTransactionDto;
import com.nomoney.paymybuddy.dto.InternalTransactionDto;
import com.nomoney.paymybuddy.model.Transaction;

import java.util.List;

/**
 * Service interface for handling transactions
 */
public interface TransactionService {

    /**
     * Create an internal transaction
     *
     * @param internalTransactionDto DTO containing the details of the internal transaction
     * @return The created internal transaction
     */
    Transaction createInternalTransaction(InternalTransactionDto internalTransactionDto);

    /**
     * Withdraw or add money to you balance
     *
     * @param externalTransactionDto DTO containing the details of the external transaction
     * @return The created transaction
     */
    Transaction setMoneyAvailable(ExternalTransactionDto externalTransactionDto);

    /**
     * Get all transactions associated with a user
     *
     * @param email The email of the user
     * @return List of transactions associated with the user
     */
    List<Transaction> getAllTransactionsByUser(String email);
}

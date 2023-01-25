package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.dto.ExternalTransactionDto;
import com.nomoney.paymybuddy.dto.InternalTransactionDto;
import com.nomoney.paymybuddy.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createInternalTransaction(InternalTransactionDto internalTransactionDto);

    Transaction setMoneyAvailable(ExternalTransactionDto externalTransactionDto);

    List<Transaction> getAllTransactionsByUser(String email);
}

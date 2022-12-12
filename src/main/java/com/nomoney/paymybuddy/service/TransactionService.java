package com.nomoney.paymybuddy.service;

import com.nomoney.paymybuddy.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction create(Transaction transaction);

    List<Transaction> getAllTransactionsByUser(String email);
}

package com.nomoney.paymybuddy.repository;

import com.nomoney.paymybuddy.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for the {@link Transaction} entity.
 * The repository extends {@link JpaRepository} and provides methods for fetching transactions based on the user email.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find all transactions made by a user with the specified email
     *
     * @param userEmail email of the user
     * @return list of transactions made by the user
     */
    List<Transaction> findAllByUserEmail(String userEmail);
}

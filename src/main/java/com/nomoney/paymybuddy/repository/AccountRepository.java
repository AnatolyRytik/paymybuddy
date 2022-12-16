package com.nomoney.paymybuddy.repository;

import com.nomoney.paymybuddy.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByEmail(String email);
}

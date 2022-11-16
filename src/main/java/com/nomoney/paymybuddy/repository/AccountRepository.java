package com.nomoney.paymybuddy.repository;

import com.nomoney.paymybuddy.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}

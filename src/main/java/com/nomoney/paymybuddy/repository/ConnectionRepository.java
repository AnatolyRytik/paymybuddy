package com.nomoney.paymybuddy.repository;

import com.nomoney.paymybuddy.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
}

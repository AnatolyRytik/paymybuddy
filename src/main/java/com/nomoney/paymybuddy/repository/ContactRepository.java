package com.nomoney.paymybuddy.repository;

import com.nomoney.paymybuddy.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}

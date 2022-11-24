package com.nomoney.paymybuddy.repository;

import com.nomoney.paymybuddy.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Set<Contact> findAllByUserEmail(String userEmail);
}

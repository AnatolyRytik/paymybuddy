package com.nomoney.paymybuddy.repository;

import com.nomoney.paymybuddy.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Contact} entities.
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    /**
     * Retrieves a list of all {@link Contact} entities associated with a given user email.
     *
     * @param userEmail the email of the user whose contacts are to be retrieved
     * @return a list of {@link Contact} entities associated with the given user email
     */
    List<Contact> findAllByUserEmail(String userEmail);
}

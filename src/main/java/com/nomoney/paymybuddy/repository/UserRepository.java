package com.nomoney.paymybuddy.repository;

import com.nomoney.paymybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The UserRepository interface is a Spring Data JPA repository for the {@link User} entity.
 * It provides basic CRUD operations as well as additional methods for finding a user by email.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by email.
     *
     * @param email the email of the user
     * @return an {@link Optional} containing the user or empty if no user is found
     */
    Optional<User> findByEmail(String email);
}

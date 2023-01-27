package com.nomoney.paymybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The Contact class represents a contact between two users.
 * It is a JPA Entity that maps to the 'connections' table in the database.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "connections")
public class Contact extends AbstractBaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;
}

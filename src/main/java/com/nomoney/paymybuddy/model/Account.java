package com.nomoney.paymybuddy.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "accounts")
public class Account extends AbstractBaseEntity {

    @Email
    @JoinColumn(name = "user_email", nullable = false)
    private String userEmail;

    @NotBlank
    @Column(name = "balance", nullable = false)
    private double balance;
}

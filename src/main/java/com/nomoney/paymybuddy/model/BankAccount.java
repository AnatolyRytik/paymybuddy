package com.nomoney.paymybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Entity
@Table(name = "accounts")
@NoArgsConstructor
public class BankAccount extends AbstractBaseEntity {

    @Email
    @JoinColumn(name = "user_email", nullable = false)
    private String userEmail;

    @NotBlank
    @Column(name = "iban", nullable = false)
    private String iban;
}

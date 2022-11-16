package com.nomoney.paymybuddy.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Entity
@Table(name = "transactions")
public class Transaction extends AbstractBaseEntity {

    @NotBlank
    private double amount;

    @Email
    private String userEmail;

    @Email
    private String emailRecipient;

    private Date time = new Date();
}

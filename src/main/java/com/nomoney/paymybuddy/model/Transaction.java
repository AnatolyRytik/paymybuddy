package com.nomoney.paymybuddy.model;

import com.nomoney.paymybuddy.dto.ExternalTransactionDto;
import com.nomoney.paymybuddy.dto.InternalTransactionDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Entity
@Table(name = "transactions")
@NoArgsConstructor
public class Transaction extends AbstractBaseEntity {

    @NotBlank
    private double amount;

    @Email
    private String userEmail;

    @Email
    private String emailRecipient;

    private String userIban;

    private Date time = new Date();

    public Transaction(ExternalTransactionDto externalTransactionDto) {
        this.amount = externalTransactionDto.getAmount();
        this.userEmail = externalTransactionDto.getUserEmail();
        this.userIban = externalTransactionDto.getUserIban();
        this.emailRecipient = "";
        this.time = new Date();
    }

    public Transaction(InternalTransactionDto internalTransactionDto) {
        this.amount = internalTransactionDto.getAmount();
        this.userEmail = internalTransactionDto.getUserEmail();
        this.emailRecipient = internalTransactionDto.getEmailRecipient();
        this.userIban = "";
        this.time = new Date();
    }
}

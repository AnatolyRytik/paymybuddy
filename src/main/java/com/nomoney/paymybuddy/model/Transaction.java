package com.nomoney.paymybuddy.model;

import com.nomoney.paymybuddy.dto.ExternalTransactionDto;
import com.nomoney.paymybuddy.dto.InternalTransactionDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import java.util.Date;

/**
 * This class represents a transaction. It extends the AbstractBaseEntity class and
 * is annotated with @Entity and @Table to indicate that it is a JPA entity
 * and is mapped to the 'transactions' table in the database.
 *
 * @see AbstractBaseEntity
 */
@Data
@Entity
@Table(name = "transactions")
@NoArgsConstructor
public class Transaction extends AbstractBaseEntity {

    private double amount;

    @Email
    @Column(name = "user_email")
    private String userEmail;

    @Email
    @Column(name = "email_recipient")
    private String emailRecipient;

    @Column(name = "user_iban")
    private String userIban;

    @Column(name = "date_time")
    private Date time = new Date();

    /**
     * Creates a new Transaction object using the data from an ExternalTransactionDto object.
     *
     * @param externalTransactionDto The ExternalTransactionDto object containing the data for the new Transaction object.
     */
    public Transaction(ExternalTransactionDto externalTransactionDto) {
        this.amount = externalTransactionDto.getAmountToWithdraw() == 0.0 ? externalTransactionDto.getAmountToAdd() : externalTransactionDto.getAmountToWithdraw();
        this.userEmail = externalTransactionDto.getUserEmail();
        this.userIban = externalTransactionDto.getUserIban();
        this.emailRecipient = "";
        this.time = new Date();
    }

    /**
     * Creates a new Transaction object using the data from an InternalTransactionDto object.
     *
     * @param internalTransactionDto The InternalTransactionDto object containing the data for the new Transaction object.
     */
    public Transaction(InternalTransactionDto internalTransactionDto) {
        this.amount = internalTransactionDto.getAmount();
        this.userEmail = internalTransactionDto.getUserEmail();
        this.emailRecipient = internalTransactionDto.getEmailRecipient();
        this.userIban = "";
        this.time = new Date();
    }
}

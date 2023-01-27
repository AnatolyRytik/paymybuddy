package com.nomoney.paymybuddy.model;

import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * The User class represents a user of the system and stores information about that user.
 * <p>
 * It extends the AbstractBaseEntity class which provides basic properties such as an ID.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractBaseEntity {

    @Column(name = "firstname")
    protected String firstname;

    @Column(name = "lastname")
    protected String lastname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "balance")
    private double balance;

    @Column(name = "registered")
    private Date registered = new Date();

    /**
     * Constructor that accepts a UserRegistrationDto and sets the fields of the User based on the Dto's fields.
     *
     * @param userRegistrationDto The Dto to use for creating the User.
     */
    public User(UserRegistrationDto userRegistrationDto) {
        this.firstname = userRegistrationDto.getFirstname();
        this.lastname = userRegistrationDto.getLastname();
        this.email = userRegistrationDto.getEmail();
        this.password = userRegistrationDto.getPassword();
        this.balance = 0.0;
        this.registered = new Date();
    }

    /**
     * Setter method for the email field.
     * It will convert the email to lowercase before storing it.
     *
     * @param email the email of the user.
     */
    public void setEmail(String email) {
        this.email = StringUtils.hasLength(email) ? null : email.toLowerCase();
    }
}
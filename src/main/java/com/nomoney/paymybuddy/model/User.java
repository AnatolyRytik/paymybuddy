package com.nomoney.paymybuddy.model;

import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
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

    public User(UserRegistrationDto userRegistrationDto) {
        this.firstname = userRegistrationDto.getFirstname();
        this.lastname = userRegistrationDto.getLastname();
        this.email = userRegistrationDto.getEmail();
        this.password =  userRegistrationDto.getPassword();
        this.balance = 0.0;
        this.registered = new Date();
    }

    public void setEmail(String email) {
        this.email = StringUtils.hasLength(email) ? null : email.toLowerCase();
    }
}
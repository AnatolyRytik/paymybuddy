package com.nomoney.paymybuddy.model;

import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractBaseEntity {

    @NotBlank
    @Column(name = "firstname")
    protected String firstName;

    @NotBlank
    @Column(name = "lastname")
    protected String lastName;

    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "registered")
    private Date registered = new Date();

    public User(UserRegistrationDto userRegistrationDto) {
        this.firstName = userRegistrationDto.getFirstname();
        this.lastName = userRegistrationDto.getLastname();
        this.email = userRegistrationDto.getPassword();
        this.password = userRegistrationDto.getPassword();
        this.registered = new Date();
    }

    public void setEmail(String email) {
        this.email = StringUtils.hasLength(email) ? null : email.toLowerCase();
    }
}
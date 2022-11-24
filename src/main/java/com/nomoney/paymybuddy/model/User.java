package com.nomoney.paymybuddy.model;

import com.nomoney.paymybuddy.dto.UserRegistrationDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractBaseEntity {

    @NotBlank
    protected String firstName;

    @NotBlank
    protected String lastName;

    @Email
    private String email;

    private String password;

    @NotNull
    private Date registered = new Date();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Contact> relations;

    public User(UserRegistrationDto userRegistrationDto) {
        this.firstName = userRegistrationDto.getFirstname();
        this.lastName = userRegistrationDto.getLastname();
        this.email = userRegistrationDto.getPassword();
        this.password = userRegistrationDto.getPassword();
        this.registered = new Date();
        this.relations = Collections.emptySet();
    }

    public void setEmail(String email) {
        this.email = StringUtils.hasLength(email) ? null : email.toLowerCase();
    }
}
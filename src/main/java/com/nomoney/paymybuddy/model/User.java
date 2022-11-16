package com.nomoney.paymybuddy.model;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class User extends AbstractBaseEntity {

    @NotBlank
    protected String name;

    @Email
    private String email;

    private String password;

    @NotNull
    private Date registered = new Date();

    public void setEmail(String email) {
        this.email = StringUtils.hasLength(email) ? null : email.toLowerCase();
    }
}
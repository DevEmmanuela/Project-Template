package com.emmanuela.newecommerce.entities;

import com.emmanuela.newecommerce.enums.UsersStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class Users extends BaseClass{
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String confirmPassword;
    private String role;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UsersStatus usersStatus;
}

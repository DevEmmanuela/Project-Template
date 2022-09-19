package com.emmanuela.newecommerce.entities;

import com.emmanuela.newecommerce.enums.AuthenticationProvider;
import com.emmanuela.newecommerce.enums.UsersStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

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

    @Size(min = 8, message = "Minimum password length is 8")
    private String password;

    @Size(min = 8, message = "Minimum password length is 8")
    private String confirmPassword;

    @Enumerated(EnumType.STRING)
    private AuthenticationProvider authenticationProvider;

    private String role;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UsersStatus usersStatus;
}

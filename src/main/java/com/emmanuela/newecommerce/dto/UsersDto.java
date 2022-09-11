package com.emmanuela.newecommerce.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UsersDto {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String confirmPassword;
}

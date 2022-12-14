package com.emmanuela.newecommerce.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UsersRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
}

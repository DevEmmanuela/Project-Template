package com.emmanuela.newecommerce.request;

import lombok.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
    @Size(min = 8,  message = "Minimum password length is 8")
    private String newPassword;
    @Size(min = 8, message = "Minimum password length is 8")
    private String confirmPassword;
}

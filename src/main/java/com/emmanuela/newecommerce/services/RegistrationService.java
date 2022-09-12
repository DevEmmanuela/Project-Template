package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.dto.UsersDto;
import com.emmanuela.newecommerce.entities.Users;

public interface RegistrationService {
    String registerUser(UsersDto usersDto);
    void sendMailVerificationLink(String name, String email, String link);
    String confirmToken(String token);
    void resendVerificationEmail (Users users);
}

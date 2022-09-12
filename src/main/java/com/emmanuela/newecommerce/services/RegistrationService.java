package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.response.UsersResponse;
import com.emmanuela.newecommerce.entities.Users;

public interface RegistrationService {
    String registerUser(UsersResponse usersResponse);
    void sendMailVerificationLink(String name, String email, String link);
    String confirmToken(String token);
    void resendVerificationEmail (Users users);
}

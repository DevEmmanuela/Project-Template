package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.request.UsersRequest;
import com.emmanuela.newecommerce.entities.Users;

public interface RegistrationService {
    String registerUser(UsersRequest usersRequest);
    void sendMailVerificationLink(String name, String email, String link);
    String confirmToken(String token);
    void resendVerificationEmail (Users users);
}

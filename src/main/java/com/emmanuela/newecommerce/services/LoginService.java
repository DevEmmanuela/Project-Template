package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.request.ForgotPasswordRequest;
import com.emmanuela.newecommerce.request.LoginRequest;

public interface LoginService {
    String login(LoginRequest loginRequest);

    String generateResetToken(ForgotPasswordRequest forgotPasswordRequest);
}

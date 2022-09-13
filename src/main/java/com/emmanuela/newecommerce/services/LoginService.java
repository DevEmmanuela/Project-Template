package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.request.ForgotPasswordRequest;
import com.emmanuela.newecommerce.request.LoginRequest;
import com.emmanuela.newecommerce.request.ResetPasswordRequest;

public interface LoginService {
    String login(LoginRequest loginRequest);

    String generateResetToken(ForgotPasswordRequest forgotPasswordRequest);
    String resetPassword(ResetPasswordRequest resetPasswordRequest, String token);
}

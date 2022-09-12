package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.validationtoken.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken token);
    Optional<ConfirmationToken> getToken(String token);
    void setConfirmedAt(String token);
}

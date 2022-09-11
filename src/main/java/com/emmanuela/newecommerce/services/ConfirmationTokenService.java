package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.validationtoken.ConfirmationToken;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken token);
}

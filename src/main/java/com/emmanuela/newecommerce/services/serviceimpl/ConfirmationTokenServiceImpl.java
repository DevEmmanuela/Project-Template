package com.emmanuela.newecommerce.services.serviceimpl;

import com.emmanuela.newecommerce.repository.ConfirmationTokenRepository;
import com.emmanuela.newecommerce.services.ConfirmationTokenService;
import com.emmanuela.newecommerce.validationtoken.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);

    }
}

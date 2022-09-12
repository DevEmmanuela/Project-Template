package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.response.SendMailResponse;
import org.springframework.mail.MailException;

public interface MailService {
    String sendMail(SendMailResponse sendMailResponse) throws MailException;
}

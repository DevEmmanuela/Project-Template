package com.emmanuela.newecommerce.services;

import com.emmanuela.newecommerce.dto.SendMailDto;
import org.springframework.mail.MailException;

public interface MailService {
    String sendMail(SendMailDto sendMailDto) throws MailException;
}

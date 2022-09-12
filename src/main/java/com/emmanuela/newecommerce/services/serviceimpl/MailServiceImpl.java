package com.emmanuela.newecommerce.services.serviceimpl;

import com.emmanuela.newecommerce.customexceptions.FailedMailException;
import com.emmanuela.newecommerce.response.SendMailResponse;
import com.emmanuela.newecommerce.services.MailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    private final String FOOTER_TEMPLATE = "\n\n Regards\n Activity Tracker Team!";
    @Override
    public String sendMail(SendMailResponse sendMailResponse) throws MailException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(sendMailResponse.getTo());
        simpleMailMessage.setSubject(sendMailResponse.getSubject());
        simpleMailMessage.setText("Hi, " + sendMailResponse.getName() + "\n\n" + sendMailResponse.getBody() + FOOTER_TEMPLATE);

        try{
            javaMailSender.send(simpleMailMessage);
            return "Mail sent successfully";
        }catch(MailException ex){
            throw new FailedMailException("Mail could not send as a result of " + ex.getMessage());
        }

    }
}

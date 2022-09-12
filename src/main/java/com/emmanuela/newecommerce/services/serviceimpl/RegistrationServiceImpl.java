package com.emmanuela.newecommerce.services.serviceimpl;

import com.emmanuela.newecommerce.customexceptions.EmailAlreadyConfirmedException;
import com.emmanuela.newecommerce.customexceptions.TokenNotFoundException;
import com.emmanuela.newecommerce.customexceptions.UserNotFoundException;
import com.emmanuela.newecommerce.dto.SendMailDto;
import com.emmanuela.newecommerce.dto.UsersDto;
import com.emmanuela.newecommerce.entities.Users;
import com.emmanuela.newecommerce.repository.UsersRepository;
import com.emmanuela.newecommerce.services.RegistrationService;
import com.emmanuela.newecommerce.util.Constant;
import com.emmanuela.newecommerce.validationtoken.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UsersServiceImpl usersService;
    private final MailServiceImpl mailService;
    private final ConfirmationTokenServiceImpl confirmationTokenService;
    private final UsersRepository usersRepository;


    @Override
    public String registerUser(UsersDto usersDto) {
        String token = usersService.registerUser(usersDto);

        String link = Constant.EMAIL_VERIFICATION_LINK + token;
        sendMailVerificationLink(usersDto.getFirstname(), usersDto.getEmail(), link);
        return "please check your email for account activation link";
    }

    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));

        if(confirmationToken.getConfirmedAt() != null){
            throw new EmailAlreadyConfirmedException("Email already confirmed.");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())){
            Users users = usersRepository.findByEmail(confirmationToken.getUsers().getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            usersService.deleteUnverifiedToken(confirmationToken);
            resendVerificationEmail(users);
            return "Previous verification token expired, check email for new token.";
        }

        confirmationTokenService.setConfirmedAt(token);
        usersService.enableUser(confirmationToken.getUsers().getEmail());
        return "Email confirmed";
    }

    @Override
    public void resendVerificationEmail(Users users) {
        String token = UUID.randomUUID().toString();
        String link = Constant.EMAIL_VERIFICATION_LINK + token;
        sendMailVerificationLink(users.getFirstname(), users.getEmail(), link);
        usersService.saveToken(token, users);
    }

    @Override
    public void sendMailVerificationLink(String name, String email, String link) {
        String subject = "Email verification";
        String body = "Click the link below to verify your email \n" + link;
        SendMailDto sendMailDto = new SendMailDto(email,name, subject, body);
        mailService.sendMail(sendMailDto);
    }
}

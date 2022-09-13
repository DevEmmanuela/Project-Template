package com.emmanuela.newecommerce.services.serviceimpl;

import com.emmanuela.newecommerce.customexceptions.PasswordNotMatchException;
import com.emmanuela.newecommerce.customexceptions.UserNotFoundException;
import com.emmanuela.newecommerce.entities.Users;
import com.emmanuela.newecommerce.enums.UsersStatus;
import com.emmanuela.newecommerce.repository.UsersRepository;
import com.emmanuela.newecommerce.request.ForgotPasswordRequest;
import com.emmanuela.newecommerce.request.LoginRequest;
import com.emmanuela.newecommerce.request.ResetPasswordRequest;
import com.emmanuela.newecommerce.response.SendMailResponse;
import com.emmanuela.newecommerce.security.filter.JwtUtils;
import com.emmanuela.newecommerce.services.LoginService;
import com.emmanuela.newecommerce.util.Constant;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {
    private final UsersRepository usersRepository;
    private final AuthenticationManager authenticationManager;
    private final UsersServiceImpl usersService;
    private final JwtUtils jwtUtils;
    private final MailServiceImpl mailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public String login(LoginRequest loginRequest) {
        Users users = usersRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(users.getUsersStatus() != UsersStatus.ACTIVE){
            return "please, verify your account from your email to continue";
        }

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (loginRequest.getEmail(), loginRequest.getPassword()));
        }catch(BadCredentialsException ex){
            throw new UsernameNotFoundException("Invalid Credential");
        }

        final UserDetails userDetails = usersService.loadUserByUsername(loginRequest.getEmail());
        return jwtUtils.generateToken(userDetails);
    }
    @Override
    public String generateResetToken(ForgotPasswordRequest forgotPasswordRequest) {
        String email = forgotPasswordRequest.getEmail();
        Users users = usersRepository.findUsersByEmail(email);
        if(users == null){
            throw new UsernameNotFoundException("User not found");
        }

        String token = jwtUtils.generatePasswordResetToken(email);
        sendPasswordResetEmail(users, token);
        return "check your mail to reset your password";
    }
    private void sendPasswordResetEmail(Users users, String url) {
        String subject = "Reset your password";
        String senderName = users.getLastname() ;
        String mailContent = "Please click on the link below to reset your password \n";
        mailContent += Constant.RESET_PASSWORD_LINK + url;

        SendMailResponse sendMailResponse = new SendMailResponse(users.getEmail(), senderName, subject, mailContent);
        mailService.sendMail(sendMailResponse);
    }

    @Override
    public String resetPassword(ResetPasswordRequest resetPasswordRequest, String token) {
        if(!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())){
            throw new PasswordNotMatchException("Password does not match");
        }

        String email = jwtUtils.extractUsername(token);
        Users users = usersRepository.findUsersByEmail(email);
        if(users == null){
            throw new UserNotFoundException("User not found");
        }
        users.setPassword(bCryptPasswordEncoder.encode(resetPasswordRequest.getNewPassword()));
        usersRepository.save(users);
        return "Password reset successfully";
    }



}

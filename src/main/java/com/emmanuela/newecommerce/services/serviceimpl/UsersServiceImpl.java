package com.emmanuela.newecommerce.services.serviceimpl;

import com.emmanuela.newecommerce.customexceptions.EmailAlreadyExistException;
import com.emmanuela.newecommerce.customexceptions.PasswordNotMatchException;
import com.emmanuela.newecommerce.customexceptions.UserNotFoundException;
import com.emmanuela.newecommerce.response.UsersResponse;
import com.emmanuela.newecommerce.entities.Users;
import com.emmanuela.newecommerce.enums.UsersStatus;
import com.emmanuela.newecommerce.repository.ConfirmationTokenRepository;
import com.emmanuela.newecommerce.repository.UsersRepository;
import com.emmanuela.newecommerce.services.UsersService;
import com.emmanuela.newecommerce.validationtoken.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UserDetailsService, UsersService {
    private final UsersRepository usersRepository;
    private final String USER_EMAIL_ALREADY_EXISTS_MSG = "Users with email %s already exists!";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenServiceImpl confirmationTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = usersRepository.findUsersByEmail(email);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");

        if(users == null){
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        else{
            log.info("User found");
            return new User(users.getEmail(), users.getPassword(), Collections.singleton(authority));
        }
    }

    @Override
    public String registerUser(UsersResponse usersResponse) {
        if(!usersResponse.getPassword().equals(usersResponse.getConfirmPassword())){
            throw new PasswordNotMatchException("password does not match");
        }
        Optional<Users> users = usersRepository.findByEmail(usersResponse.getEmail());

        if(users.isPresent()){
            throw new EmailAlreadyExistException(
                    String.format(USER_EMAIL_ALREADY_EXISTS_MSG, usersResponse.getEmail()));
        }
        Users users1 = new Users();
        users1.setFirstname(usersResponse.getFirstname());
        users1.setLastname(usersResponse.getLastname());
        users1.setEmail(usersResponse.getEmail());
        users1.setPassword(bCryptPasswordEncoder.encode(usersResponse.getPassword()));
        users1.setConfirmPassword(bCryptPasswordEncoder.encode(usersResponse.getConfirmPassword()));
        users1.setRole("USER");
        users1.setUsersStatus(UsersStatus.INACTIVE);
        users1.setCreatedAt(LocalDateTime.now());

        String token = UUID.randomUUID().toString();
        saveToken(token, users1);

        return token;
    }

    @Override
    public void saveToken(String token, Users users) {
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24),
                users);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
    }

    @Override
    public void deleteUnverifiedToken(ConfirmationToken token) {
        confirmationTokenRepository.delete(token);
    }

    @Override
    public void enableUser(String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        users.setUsersStatus(UsersStatus.ACTIVE);
        usersRepository.save(users);
    }


}

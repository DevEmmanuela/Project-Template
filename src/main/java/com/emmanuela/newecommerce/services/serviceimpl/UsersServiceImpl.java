package com.emmanuela.newecommerce.services.serviceimpl;

import com.emmanuela.newecommerce.customexceptions.EmailAlreadyExistException;
import com.emmanuela.newecommerce.customexceptions.PasswordNotMatchException;
import com.emmanuela.newecommerce.dto.UsersDto;
import com.emmanuela.newecommerce.entities.Users;
import com.emmanuela.newecommerce.enums.UsersStatus;
import com.emmanuela.newecommerce.repository.UsersRepository;
import com.emmanuela.newecommerce.services.UsersService;
import com.emmanuela.newecommerce.validationtoken.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UserDetailsService, UsersService {
    private final UsersRepository usersRepository;
    private final String USER_EMAIL_ALREADY_EXISTS_MSG = "Users with email %s already exists!";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenServiceImpl confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return null;
    }

    @Override
    public String registerUser(UsersDto usersDto) {
        if(!usersDto.getPassword().equals(usersDto.getConfirmPassword())){
            throw new PasswordNotMatchException("password does not match");
        }
        Optional<Users> users = usersRepository.findByEmail(usersDto.getEmail());

        if(users.isPresent()){
            throw new EmailAlreadyExistException(
                    String.format(USER_EMAIL_ALREADY_EXISTS_MSG, usersDto.getEmail()));
        }
        Users users1 = new Users();
        users1.setFirstname(usersDto.getFirstname());
        users1.setLastname(usersDto.getLastname());
        users1.setEmail(usersDto.getEmail());
        users1.setPassword(bCryptPasswordEncoder.encode(usersDto.getPassword()));
        users1.setConfirmPassword(bCryptPasswordEncoder.encode(usersDto.getConfirmPassword()));
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


}

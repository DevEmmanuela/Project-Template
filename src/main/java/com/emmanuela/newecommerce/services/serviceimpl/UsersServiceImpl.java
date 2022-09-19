package com.emmanuela.newecommerce.services.serviceimpl;

import com.emmanuela.newecommerce.customexceptions.EmailAlreadyExistException;
import com.emmanuela.newecommerce.customexceptions.PasswordNotMatchException;
import com.emmanuela.newecommerce.customexceptions.UserNotFoundException;
import com.emmanuela.newecommerce.enums.AuthenticationProvider;
import com.emmanuela.newecommerce.request.UsersRequest;
import com.emmanuela.newecommerce.entities.Users;
import com.emmanuela.newecommerce.enums.UsersStatus;
import com.emmanuela.newecommerce.repository.ConfirmationTokenRepository;
import com.emmanuela.newecommerce.repository.UsersRepository;
import com.emmanuela.newecommerce.response.UsersResponse;
import com.emmanuela.newecommerce.security.oauth.CustomOAuth2User;
import com.emmanuela.newecommerce.services.UsersService;
import com.emmanuela.newecommerce.validationtoken.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
            return new User(users.getEmail(), users.getPassword(), Collections.singleton(authority));
        }
    }

    @Override
    public String registerUser(UsersRequest usersRequest) {
        if(!usersRequest.getPassword().equals(usersRequest.getConfirmPassword())){
            throw new PasswordNotMatchException("password does not match");
        }
        Optional<Users> users = usersRepository.findByEmail(usersRequest.getEmail());

        if(users.isPresent()){
            throw new EmailAlreadyExistException(
                    String.format(USER_EMAIL_ALREADY_EXISTS_MSG, usersRequest.getEmail()));
        }
        Users users1 = new Users();
        users1.setFirstname(usersRequest.getFirstname());
        users1.setLastname(usersRequest.getLastname());
        users1.setEmail(usersRequest.getEmail());
        users1.setPassword(bCryptPasswordEncoder.encode(usersRequest.getPassword()));
        users1.setConfirmPassword(bCryptPasswordEncoder.encode(usersRequest.getConfirmPassword()));
        users1.setRole("USER");
        users1.setUsersStatus(UsersStatus.INACTIVE);
        users1.setCreatedAt(LocalDateTime.now());
        users1.setPhoneNumber(usersRequest.getPhoneNumber());

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

    @Override
    public UsersResponse getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users user1 = usersRepository.findUsersByEmail(user.getUsername());
        UsersResponse usersResponse = UsersResponse.builder()
                .firstname(user1.getFirstname())
                .lastname(user1.getLastname())
                .email(user1.getEmail())
                .phoneNumber(user1.getPhoneNumber())
                .build();
        return usersResponse;
    }

    @Override
    public String getUsername() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersRepository.findUsersByEmail(userName);
        return "Hi, " + users.getFirstname();
    }

    @Override
    public void createOAuthUser(CustomOAuth2User oAuth2User, Authentication authentication) {
        Users userExist = usersRepository.findUsersByEmail(oAuth2User.getEmail());
        String[] result = oAuth2User.getName().split(" ");
        String lastName = result[result.length-1];

        if(userExist == null){
            Users newOauthUser = new Users();
            newOauthUser.setFirstname(result[0]);
            newOauthUser.setLastname(lastName);
            newOauthUser.setEmail(oAuth2User.getEmail());
            newOauthUser.setAuthenticationProvider(AuthenticationProvider.GOOGLE);
            newOauthUser.setRole("USER");
            newOauthUser.setPassword(bCryptPasswordEncoder.encode(oAuth2User.getName()));
            newOauthUser.setConfirmPassword(bCryptPasswordEncoder.encode(oAuth2User.getName()));
            newOauthUser.setUsersStatus(UsersStatus.ACTIVE);
            newOauthUser.setCreatedAt(LocalDateTime.now());

            usersRepository.save(newOauthUser);
        }
    }

    @Override
    public void updateOAuth(Users users, CustomOAuth2User oAuth2User, Authentication authentication) {

        String[] result = oAuth2User.getName().split(" ");
        String lastName = result[result.length-1];

        users.setFirstname(result[0]);
        users.setLastname(lastName);
        users.setAuthenticationProvider(AuthenticationProvider.GOOGLE);

        usersRepository.save(users);
    }

}

package com.emmanuela.newecommerce.services.serviceimpl;

import com.emmanuela.newecommerce.customexceptions.UserNotFoundException;
import com.emmanuela.newecommerce.entities.Users;
import com.emmanuela.newecommerce.enums.UsersStatus;
import com.emmanuela.newecommerce.repository.UsersRepository;
import com.emmanuela.newecommerce.request.LoginRequest;
import com.emmanuela.newecommerce.security.filter.JwtUtils;
import com.emmanuela.newecommerce.services.LoginService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {
    private final UsersRepository usersRepository;
    private final AuthenticationManager authenticationManager;
    private final UsersServiceImpl usersService;
    private final JwtUtils jwtUtils;
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
}

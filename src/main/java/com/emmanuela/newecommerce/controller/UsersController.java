package com.emmanuela.newecommerce.controller;

import com.emmanuela.newecommerce.request.LoginRequest;
import com.emmanuela.newecommerce.response.LoginResponse;
import com.emmanuela.newecommerce.request.UsersRequest;
import com.emmanuela.newecommerce.response.UsersResponse;
import com.emmanuela.newecommerce.services.serviceimpl.LoginServiceImpl;
import com.emmanuela.newecommerce.services.serviceimpl.RegistrationServiceImpl;
import com.emmanuela.newecommerce.services.serviceimpl.UsersServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UsersController {

    private final RegistrationServiceImpl registrationService;
    private final LoginServiceImpl loginService;
    private final UsersServiceImpl usersService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UsersRequest usersRequest){
        return new ResponseEntity<>(registrationService.registerUser(usersRequest), HttpStatus.OK);
    }
    @GetMapping("/confirm-token")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token){
        return new ResponseEntity<>(registrationService.confirmToken(token), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>( new LoginResponse(loginService.login(loginRequest)), HttpStatus.OK);
    }

    @GetMapping("/get-user")
    public ResponseEntity<UsersResponse> getUser(){
        return new ResponseEntity<>(usersService.getUser(), HttpStatus.OK);
    }

    @GetMapping("/get-username")
    public ResponseEntity<String> getUsername(){
        return new ResponseEntity<>(usersService.getUsername(), HttpStatus.OK);
    }
}

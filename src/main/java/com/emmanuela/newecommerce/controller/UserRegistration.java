package com.emmanuela.newecommerce.controller;

import com.emmanuela.newecommerce.dto.UsersDto;
import com.emmanuela.newecommerce.services.serviceimpl.RegistrationServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserRegistration {

    private final RegistrationServiceImpl registrationService;

    @PostMapping("/register")
    public String register(@Valid @RequestBody UsersDto usersDto){
        return registrationService.registerUser(usersDto);
    }
    @GetMapping("/confirm-token")
    public String confirmToken(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }
}

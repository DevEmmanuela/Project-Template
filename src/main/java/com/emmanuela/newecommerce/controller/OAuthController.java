package com.emmanuela.newecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @GetMapping("/index")
    public String socialRegister(){
        return " successfully signed in";
    }


}

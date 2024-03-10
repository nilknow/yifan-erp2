package com.nilknow.yifanerp2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/login")
@Controller
public class LoginPageController {
    @GetMapping("/")
    public String index(){
        return "page/login";
    }

    @GetMapping("/fail")
    public String fail(){
        return "page/login-fail";
    }
}

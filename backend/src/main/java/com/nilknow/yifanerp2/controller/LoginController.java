package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.service.LoginUserService;
import com.nilknow.yifanerp2.util.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/login")
@RestController
public class LoginController {
    @Resource
    private LoginUserService loginUserService;

    @PostMapping
    public String login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        String companyId = request.getHeader("Company-ID");
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        //todo parameter validation
        boolean authenticate = loginUserService.authenticate(username, password, Long.valueOf(companyId));
        if (authenticate) {
            return jwtToken(username, companyId);
        } else {
            return "";
        }
    }

    @Data
    private static class LoginDTO{
        private String username;
        private String password;
    }

    private String jwtToken(String username, String companyId) {
        String jwtToken = JwtUtil.generateToken(username, Long.valueOf(companyId));
        return "Bearer " + jwtToken;
    }
}

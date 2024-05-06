package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.config.security.UserIdHolder;
import com.nilknow.yifanerp2.entity.LoginUser;
import com.nilknow.yifanerp2.exception.ResException;
import com.nilknow.yifanerp2.service.LoginUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class LoginUserController {
    @Resource
    private LoginUserService loginUserService;

    @GetMapping("/info")
    public Res<LoginUser> info(HttpServletResponse response) throws ResException {
        Long userId = UserIdHolder.get();
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Optional<LoginUser> userOpt = loginUserService.findById(userId);
        if (userOpt.isPresent()) {
            return new Res<LoginUser>().success(userOpt.get());
        } else {
            throw new ResException("No user with id " + userId);
        }
    }
}

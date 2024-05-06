package com.nilknow.yifanerp2.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequestMapping("/api/logout")
@RestController
public class LogoutController {
    @GetMapping
    public void logout(HttpServletResponse response) throws IOException {
        // Create a new cookie with the updated value
        Cookie cookie = new Cookie("Authorization", URLEncoder.encode("", StandardCharsets.UTF_8));
        cookie.setMaxAge(60 * 60 ); // 1 hour
        cookie.setPath("/"); // Make it accessible across the entire domain
//            cookie.setSecure(true); // Use secure flag for HTTPS
//            cookie.setHttpOnly(true); // Prevent access from JavaScript

        // Add the cookie to the response
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}

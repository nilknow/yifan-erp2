package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.config.security.TenantContextHolder;
import com.nilknow.yifanerp2.entity.LoginUser;
import com.nilknow.yifanerp2.service.CompanyService;
import com.nilknow.yifanerp2.service.LoginUserService;
import com.nilknow.yifanerp2.util.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RequestMapping("/api/login")
@RestController
public class LoginController {
    @Resource
    private LoginUserService loginUserService;
    @Resource
    private CompanyService companyService;

    @PostMapping
    public LoginResp login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        Long companyId = getAndSetCompanyId(request);
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        //todo parameter validation
        Optional<LoginUser> user = loginUserService.authenticate(username, password, companyId);
        if (user.isPresent()) {
            String token = jwtToken(String.valueOf(user.get().getId()), companyId);
            // Create a new cookie with the updated value
            Cookie cookie = new Cookie("Authorization", URLEncoder.encode(token, StandardCharsets.UTF_8));
            cookie.setMaxAge(60 * 60 ); // 1 hour
            cookie.setPath("/"); // Make it accessible across the entire domain
//            cookie.setSecure(true); // Use secure flag for HTTPS
//            cookie.setHttpOnly(true); // Prevent access from JavaScript

            // Add the cookie to the response
            response.addCookie(cookie);
            return new LoginResp(true, token);
        } else {
            return new LoginResp(false, "");
        }
    }



    private Long getAndSetCompanyId(HttpServletRequest request) {
        String domainName = request.getServerName();
        if (!StringUtils.hasText(domainName)) {
            return null;
        }
        String[] domainNameParts = domainName.split("\\.");
        if (domainNameParts.length < 2) {
            return null;
        }
        String prefix = domainNameParts[0];
        Long companyId = companyService.getCompanyIdByDomainPrefix(prefix);
        if (companyId != null) {
            TenantContextHolder.set(companyId);
        }
        return companyId;
    }

    @Data
    private static class LoginDTO {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    public static class LoginResp {
        private Boolean success;
        private String token;
    }

    private String jwtToken(String username, Long companyId) {
        String jwtToken = JwtUtil.generateToken(username, companyId);
        return "Bearer " + jwtToken;
    }
}

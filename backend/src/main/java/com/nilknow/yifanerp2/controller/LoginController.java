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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    public Res<String> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        Long companyId = getAndSetCompanyId(request);
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        //todo parameter validation
        Optional<LoginUser> user = loginUserService.authenticate(username, password, companyId);
        if (user.isPresent()) {
            if (user.get().getPasswordChanged()==0) {
                return new Res<String>().fail("still default password");
            }
            String token = jwtToken(String.valueOf(user.get().getId()), companyId);
            // Create a new cookie with the updated value
            Cookie cookie = new Cookie("Authorization", URLEncoder.encode(token, StandardCharsets.UTF_8));
            cookie.setMaxAge(60 * 60 ); // 1 hour
            cookie.setPath("/"); // Make it accessible across the entire domain
//            cookie.setSecure(true); // Use secure flag for HTTPS
//            cookie.setHttpOnly(true); // Prevent access from JavaScript

            // Add the cookie to the response
            response.addCookie(cookie);
            return new Res<String>().success(token);
        } else {
            return new Res<String>().fail("不存在该用户或者密码错误");
        }
    }

    @PostMapping("/reset")
    public Res<String> reset(@RequestBody ResetDto resetDto,HttpServletRequest request) throws Exception {
        Long companyId = getAndSetCompanyId(request);
        String username = resetDto.getUsername();
        String password = resetDto.getPassword();
        String newPassword = resetDto.getNewPassword();
        if (!StringUtils.hasText(newPassword)) {
            throw new Exception("新密码不能为空白");
        }
        Optional<LoginUser> userOpt = loginUserService.authenticate(username, password, companyId);
        if (userOpt.isPresent()) {
            LoginUser loginUser = userOpt.get();
            loginUser.setPassword("{bcrypt}" + passwordEncoder.encode(newPassword));
            loginUser.setPasswordChanged(1);
            loginUserService.save(loginUser);
            return new Res<String>().success("");
        } else {
            return new Res<String>().fail("不存在该用户或者密码错误");
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
    public static class LoginDTO {
        private String username;
        private String password;
    }

    @Data
    public static class ResetDto{
        private String username;
        private String password;
        private String newPassword;
    }

    private String jwtToken(String username, Long companyId) {
        String jwtToken = JwtUtil.generateToken(username, companyId);
        return "Bearer " + jwtToken;
    }
}

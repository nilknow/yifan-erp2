package com.nilknow.yifanerp2.service;

import com.nilknow.yifanerp2.entity.LoginUser;
import com.nilknow.yifanerp2.repository.LoginUserRepository;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginUserService {
    @Resource
    private LoginUserRepository loginUserRepository;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;


    public Optional<LoginUser> authenticate(@NotBlank String username, @NotBlank String password, Long companyId) {
        Optional<LoginUser> userOpt = loginUserRepository.findByUsernameAndCompanyId(username, companyId);
        if (userOpt.isEmpty()) {
            return userOpt;
        }
        if (passwordEncoder.matches(password, userOpt.get().getPassword().substring(8))) {
            return userOpt;
        } else {
            return Optional.empty();
        }
    }

    public Optional<LoginUser> findById(Long userId) {
        return loginUserRepository.findById(userId);
    }
}

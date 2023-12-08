package com.nilknow.yifanerp2.config.security;

import com.nilknow.yifanerp2.entity.LoginUser;
import com.nilknow.yifanerp2.repository.LoginUserRepository;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private LoginUserRepository loginUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<LoginUser> userOpt = loginUserRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        LoginUser user = userOpt.get();
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities().stream()
                        .map(x -> (GrantedAuthority) x::getName).toList()
        );
    }
}
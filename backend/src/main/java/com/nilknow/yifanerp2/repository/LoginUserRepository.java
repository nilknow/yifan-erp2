package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginUserRepository extends JpaRepository<LoginUser,Long> {
    Optional<LoginUser> findByUsernameAndCompanyId(String username, Long companyId);
}

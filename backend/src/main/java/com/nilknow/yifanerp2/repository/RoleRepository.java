package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByNameAndCompanyId(String name, Long companyId);
}
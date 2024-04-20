package com.nilknow.yifanerp2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nilknow.yifanerp2.config.security.TenantContextHolder;
import com.nilknow.yifanerp2.config.security.UserIdHolder;
import com.nilknow.yifanerp2.entity.ActionLog;
import com.nilknow.yifanerp2.entity.Role;
import com.nilknow.yifanerp2.exception.ResException;
import com.nilknow.yifanerp2.repository.LoginUserRepository;
import com.nilknow.yifanerp2.repository.RoleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class RoleService {

    @Resource
    private RoleRepository roleRepository;
    @Resource
    private LoginUserRepository loginUserRepository;
    @Resource
    private ActionLogService actionLogService;

    public Role create(Role role,String source) throws ResException, JsonProcessingException {
        buildRole(role);
        createValidate(role);

        ActionLog actionLog = ActionLog.add(
                "role",
                source,
                role,
                "create new role",
                loginUserRepository.findById(UserIdHolder.get()).get()
                );
        actionLogService.save(actionLog);

        return roleRepository.save(role);
    }

    private void buildRole(Role role) {
        role.setCompanyId(TenantContextHolder.get());
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
    }

    private void createValidate(Role role) throws ResException {
        Optional<Role> existingRole = roleRepository.findByNameAndCompanyId(role.getName(), role.getCompanyId());
        if (existingRole.isPresent()) {
            throw new ResException("Duplicate role found!");
        }
    }
}
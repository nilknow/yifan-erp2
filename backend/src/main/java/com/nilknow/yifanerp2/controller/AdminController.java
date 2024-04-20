package com.nilknow.yifanerp2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nilknow.yifanerp2.entity.Role;
import com.nilknow.yifanerp2.exception.ResException;
import com.nilknow.yifanerp2.service.LoginUserService;
import com.nilknow.yifanerp2.service.RoleService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Resource
    private LoginUserService loginUserService;
    @Resource
    private RoleService roleService;

    @PostMapping("/user")
    public Res<String> createUser(@RequestBody UserDto userDto) {
        //todo
        return new Res<String>().success("success");
    }

    @PostMapping("/role")
    public Res<Role> createRole(@RequestBody RoleDto role, @RequestParam String source) throws JsonProcessingException, ResException {
        Role newRole = new Role();
        newRole.setName(role.name);
        return new Res<Role>().success(roleService.create(newRole, source));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleDto {
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDto {
        private String username;
        private String password;
    }

}

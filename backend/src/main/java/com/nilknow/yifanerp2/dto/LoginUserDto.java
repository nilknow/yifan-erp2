package com.nilknow.yifanerp2.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nilknow.yifanerp2.entity.Authority;
import com.nilknow.yifanerp2.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {
    @JsonIgnore
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private List<Authority> authorities;
    private List<RouteDto> routes;
    @JsonIgnore
    private Company company;
}

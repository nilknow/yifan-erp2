package com.nilknow.yifanerp2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.service.MaterialService;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/material")
@RestController
public class MaterialController {
    @Resource
    private MaterialService materialService;

    @GetMapping("/list")
    public List<Material> list(String name) throws JsonProcessingException {
        List<Material> materials;
        if (StringUtils.hasText(name)) {
            materials = materialService.findAllByNameLike(name);
        } else {
            materials = materialService.findAll();
        }
        return materials;
    }
}

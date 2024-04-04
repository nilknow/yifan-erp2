package com.nilknow.yifanerp2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.service.MaterialService;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/material")
@RestController
public class MaterialController {
    @Resource
    private MaterialService materialService;

    @GetMapping("/list")
    public Res<List<Material>> list(String name) throws JsonProcessingException {
        List<Material> materials;
        if (StringUtils.hasText(name)) {
            materials = materialService.findAllByNameLike(name);
        } else {
            materials = materialService.findAll();
        }
        return new Res<List<Material>>().success(materials);
    }

    @PutMapping
    public Res<Material> update(@RequestBody Material material) throws Exception {
        materialService.update(material);
        return new Res<Material>().success(material);
    }

    @DeleteMapping("/{materialId}")
    public Res<String> deleteMaterial(@PathVariable Long materialId) {
        materialService.delete(materialId);
        return new Res<String>().success("success");
    }

    @PostMapping
    public Res<Material> add(@RequestBody Material material) throws Exception {
        materialService.add(material);
        return new Res<Material>().success(material);
    }
}

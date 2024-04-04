package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Category;
import com.nilknow.yifanerp2.repository.CategoryRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product_category")
public class ProductCategoryController {
    @Resource
    private CategoryRepository categoryRepository;

    @GetMapping("/list")
    public Res<List<Category>> list() {
        return new Res<List<Category>>().success(categoryRepository.findAll());
    }
}

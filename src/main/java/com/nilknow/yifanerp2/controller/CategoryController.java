package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Category;
import com.nilknow.yifanerp2.repository.CategoryRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryRepository categoryRepository;

    @PostMapping("/do/create")
    public String doCreate(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/product/page/create";
    }
}

package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Category;
import com.nilknow.yifanerp2.entity.Product;
import com.nilknow.yifanerp2.repository.CategoryRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product/page")
public class ProductPageController {

    @Resource
    private CategoryRepository categoryRepository;

    @GetMapping("/create")
    public String plan(Model model){
        model.addAttribute("product", new Product());
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoryRepository.findAll().stream().map(Category::getName).toList());
        return "page/product/create";
    }
}

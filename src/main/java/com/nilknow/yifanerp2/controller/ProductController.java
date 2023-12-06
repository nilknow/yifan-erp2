package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Product;
import com.nilknow.yifanerp2.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Resource
    private ProductService productService;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("product", new Product());
        return "page/product/add";
    }

    @PostMapping("/do-add")
    public String doAdd(@ModelAttribute Product p) {
        productService.add(p);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "page/product/list";
    }
}

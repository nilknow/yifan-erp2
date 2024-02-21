package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Material;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/material/page")
public class MaterialPageController {
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("material", new Material());
        return "page/material/add";
    }
}

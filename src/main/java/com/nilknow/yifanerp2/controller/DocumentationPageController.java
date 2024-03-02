package com.nilknow.yifanerp2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/documentation/page")
public class DocumentationPageController {
    @GetMapping("/")
    public String add(Model model) {
        return "page/documentation";
    }
}

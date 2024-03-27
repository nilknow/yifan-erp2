package com.nilknow.yifanerp2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/release/page")
public class ReleasePageController {
    @GetMapping("/")
    public String add(Model model) {
        return "page/release";
    }
}

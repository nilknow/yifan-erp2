package com.nilknow.yifanerp2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/todo/page")
public class TodoPageController {
    @GetMapping("/")
    public String index(){
        return "page/todo";
    }
}

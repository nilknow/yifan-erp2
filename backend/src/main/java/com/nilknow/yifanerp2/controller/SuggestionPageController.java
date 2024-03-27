package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.repository.SuggestionRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/suggestion/page")
public class SuggestionPageController {
    @Resource
    private SuggestionRepository suggestionRepository;
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("suggestions", suggestionRepository.findAllByOrderByCreateTimeDesc());
        return "page/suggestion";
    }
}

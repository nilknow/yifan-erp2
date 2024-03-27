package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.repository.BugRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bug-report/page")
public class BugReportPageController {
    @Resource
    private BugRepository bugRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("bugs", bugRepository.findAllByOrderByCreateTimeDesc());
        return "page/bug_report";
    }
}

package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Alert;
import com.nilknow.yifanerp2.service.AlertService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/alert")
public class AlertController {
    @Resource
    private AlertService alertService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Alert> alerts = alertService.findAll();
        model.addAttribute("alerts", alerts);
        return "/page/alert/list";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @RequestParam("state") Integer state, Model model) {
        alertService.updateState(id,state);
        return "redirect:list";
    }
}

package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Alert;
import com.nilknow.yifanerp2.service.AlertService;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/alert")
public class AlertController {
    @Resource
    private AlertService alertService;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/list")
    public String list(Model model) {
        List<Alert> alerts = alertService.findAll();
        model.addAttribute("alerts", alerts);
        model.addAttribute("email", new Email());
        String address = jdbcTemplate.queryForObject("select address from alert_email where id=?", new Object[]{1}, String.class).trim();
        model.addAttribute("currentEmail", address);
        return "page/alert/list";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @RequestParam("state") Integer state, Model model) {
        alertService.updateState(id,state);
        return "redirect:list";
    }

    @PostMapping("/change-email")
    public String changeEmail(@ModelAttribute Email email) {
        jdbcTemplate.update("update alert_email set address=? where id=1", email.address);
        return "redirect:list";
    }

    @Getter
    @Setter
    public static class Email{
        public String address;
    }
}

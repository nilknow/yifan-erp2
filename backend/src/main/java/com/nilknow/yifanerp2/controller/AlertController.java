package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.config.security.TenantContextHolder;
import com.nilknow.yifanerp2.entity.Alert;
import com.nilknow.yifanerp2.exception.ResException;
import com.nilknow.yifanerp2.service.AlertService;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alert")
public class AlertController {
    @Resource
    private AlertService alertService;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/list")
    public Res<List<Alert>> list() {
        List<Alert> alerts = alertService.findAll();
        return new Res<List<Alert>>().success(alerts);
    }

    @GetMapping("/email")
    public Res<String> email() {
        Long companyId = TenantContextHolder.get();
        String address = jdbcTemplate.queryForObject(
                "select address from alert_email where company_id=? limit 1",
                String.class, companyId).trim();
        return new Res<String>().success(address);
    }

    @PostMapping("/email")
    public Res<String> changeEmail(@RequestBody Email email) {
        Long companyId = TenantContextHolder.get();
        Long emailId = jdbcTemplate.queryForObject(
                "select id from alert_email where company_id=? limit 1",
                Long.class, companyId);
        jdbcTemplate.update("update alert_email set address=? where id=?",
                email.address, emailId);
        return new Res<String>().success("success");
    }

    @PutMapping("/{id}")
    public Res<String> update(@PathVariable("id") Long id,
                              @RequestParam("state") Integer state) throws ResException {
        Long companyId = TenantContextHolder.get();
        alertService.updateState(companyId, id, state);
        return new Res<String>().success("success");
    }

    @Getter
    @Setter
    public static class Email {
        public String address;
    }
}

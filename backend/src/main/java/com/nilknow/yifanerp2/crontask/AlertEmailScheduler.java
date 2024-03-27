package com.nilknow.yifanerp2.crontask;

import com.nilknow.yifanerp2.entity.Alert;
import com.nilknow.yifanerp2.service.AlertService;
import com.nilknow.yifanerp2.service.MailService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertEmailScheduler {
    @Resource
    private MailService mailService;
    @Resource
    private AlertService alertService;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void checkAlertAndSend() {
        List<Alert> alerts = alertService.findNotSends();
        if (alerts.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Alert a : alerts) {
            sb.append(a.getContent()).append("\n");
        }
        List<String> addresses = jdbcTemplate.queryForList("select address from alert_email limit 1",String.class);
        for (String address : addresses) {
            mailService.send(address, "库存预警", sb.toString());
        }
        alertService.markSent(alerts);
    }
}

package com.nilknow.yifanerp2.crontask;

import com.nilknow.yifanerp2.entity.Alert;
import com.nilknow.yifanerp2.service.AlertService;
import com.nilknow.yifanerp2.service.MailService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertEmailScheduler {
    @Resource
    private MailService mailService;
    @Resource
    private AlertService alertService;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void checkAlertAndSend(){
        List<Alert> alerts = alertService.findNotSends();
        if (alerts.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Alert a : alerts) {
            sb.append(a.getContent()).append("\n");
        }
        mailService.send("494939649@qq.com", "库存预警", sb.toString());
        alertService.markSent(alerts);
    }
}

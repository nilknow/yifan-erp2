package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Bug;
import com.nilknow.yifanerp2.repository.BugRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/bug-report")
public class BugReportController {
    @Resource
    private BugRepository bugRepository;

    @PostMapping("/submit")
    public String submit(String email, String phone,String priority, String content) {
        Bug bug = new Bug();
        bug.setEmail(email);
        bug.setPhone(phone);
        bug.setPriority(priority);
        bug.setContent(content);
        bug.setCreateTime(new Date());
        bugRepository.save(bug);
        if (bug.getId() != null) {
            return "success";
        } else {
            return "failed";
        }
    }
}

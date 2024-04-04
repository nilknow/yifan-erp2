package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Bug;
import com.nilknow.yifanerp2.repository.BugRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/bug")
public class BugController {
    @Resource
    private BugRepository bugRepository;

    @PostMapping("/add")
    public List<Bug> add(@RequestBody Bug bug) {
        bug.setCreateTime(new Date());
        bugRepository.save(bug);
        return list();
    }

    @GetMapping("/list")
    public List<Bug> list() {
        return bugRepository.findAllByOrderByCreateTimeDesc();
    }
}

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
    public Res<List<Bug>> add(@RequestBody Bug bug) {
        bug.setCreateTime(new Date());
        bugRepository.save(bug);
        return new Res<List<Bug>>().success(list().getBody());
    }

    @GetMapping("/list")
    public Res<List<Bug>> list() {
        return new Res<List<Bug>>()
                .success(bugRepository.findAllByOrderByCreateTimeDesc());
    }
}

package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.ActionLog;
import com.nilknow.yifanerp2.service.ActionLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/action_log")
public class ActionLogController {
    @Resource
    private ActionLogService actionLogService;
    @GetMapping("/list")
    public Res<List<ActionLog>> list() {
        return new Res<List<ActionLog>>().success(actionLogService.list());
    }
}

package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Route;
import com.nilknow.yifanerp2.service.RouteService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/route")
public class RouteController {
    @Resource
    private RouteService routeService;

    @GetMapping("/list")
    public Res<List<Route>> list(){
        return new Res<List<Route>>().success(routeService.findAll());
    }
}

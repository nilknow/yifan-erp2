package com.nilknow.yifanerp2.service;

import com.nilknow.yifanerp2.entity.Route;
import com.nilknow.yifanerp2.repository.RouteRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
    @Resource
    private RouteRepository routeRepository;

    public List<Route> findAll() {
        //todo better method with user recommend system
        return routeRepository.findAll();
    }
}

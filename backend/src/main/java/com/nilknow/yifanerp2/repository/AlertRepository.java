package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert,Long> {
    List<Alert> findAllByOrderByIdDesc();
}

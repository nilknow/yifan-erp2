package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionLogRepository extends JpaRepository<ActionLog,Long> {
    List<ActionLog> findAllByTableNameOrderByIdDesc(String tableName);

    List<ActionLog> findAllByOrderByIdDesc();
}

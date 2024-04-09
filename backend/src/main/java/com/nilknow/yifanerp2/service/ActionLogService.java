package com.nilknow.yifanerp2.service;

import com.nilknow.yifanerp2.entity.ActionLog;
import com.nilknow.yifanerp2.repository.ActionLogRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionLogService {
    @Resource
    private ActionLogRepository actionLogRepository;

    public void save(ActionLog actionLog) {
        actionLogRepository.save(actionLog);
    }

    public List<ActionLog> list(){
        return actionLogRepository.findAllByOrderByIdDesc();
    }

    public List<ActionLog> listByTableName(String tableName){
        return actionLogRepository.findAllByTableNameOrderByIdDesc(tableName);
    }

    public void saveAll(List<ActionLog> actionLogs) {
        actionLogRepository.saveAll(actionLogs);
    }
}

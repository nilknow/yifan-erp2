package com.nilknow.yifanerp2.service;

import com.nilknow.yifanerp2.dto.ActionLogDto;
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

    public List<ActionLogDto> listByTableName(String tableName){
        List<ActionLog> actionLogs = actionLogRepository.findAllByTableNameOrderByIdDesc(tableName);
        return actionLogs.stream().map(x -> new ActionLogDto()
                .setId(x.getId())
                .setBatchId(x.getBatchId())
                .setTimestamp(x.getTimestamp())
                .setEventType(x.getEventType())
                .setUsername(x.getUser().getUsername())
                .setAdditionalInfo(x.getAdditionalInfo())
                .setDescription(x.getDescription())
        ).toList();
    }

    public void saveAll(List<ActionLog> actionLogs) {
        actionLogRepository.saveAll(actionLogs);
    }
}

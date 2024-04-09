package com.nilknow.yifanerp2.service;

import com.nilknow.yifanerp2.entity.Alert;
import com.nilknow.yifanerp2.exception.ResException;
import com.nilknow.yifanerp2.repository.AlertRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AlertService {
    @Resource
    private AlertRepository alertRepository;

    public List<Alert> findAll() {
        return alertRepository.findAllByOrderByIdDesc();
    }

    public void update(Alert alert) {
        alertRepository.save(alert);
    }

    public void updateState(Long companyId, Long id, Integer state) throws ResException {
        Optional<Alert> opt = alertRepository.findById(id);
        if (opt.isEmpty()) {
            throw new ResException(id + " not exist");
        }
        Alert alert = opt.get();
        if (!alert.getCompanyId().equals(companyId)) {
            throw new ResException("You cannot change alert info in other company");
        }
        alert.setState(state);
        alertRepository.save(alert);
    }

    public List<Alert> findNotSends() {
        Alert alert = new Alert();
        alert.setEmailSent(0);
        return alertRepository.findAll(Example.of(alert));
    }

    public void markSent(List<Alert> alerts) {
        for (Alert a : alerts) {
            a.setEmailSent(1);
        }
        alertRepository.saveAll(alerts);
    }
}

package com.nilknow.yifanerp2.crontask;

import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.service.MaterialService;
import com.nilknow.yifanerp2.util.ExcelUtil;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Component
public class ArchiveScheduler {
    @Resource
    private MaterialService materialService;

    @Scheduled(cron = "0 0 0 * * *")
    public void archive() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyy");
        String now = currentDate.format(formatter);
        String fileName = "物料库存_"+now+".xlsx"; // File name
        try (FileOutputStream os = new FileOutputStream(fileName)) {
            List<Material> materials = materialService.findAll();
            ExcelUtil.exportMaterials(os, materials);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

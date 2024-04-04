package com.nilknow.yifanerp2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.service.MaterialService;
import com.nilknow.yifanerp2.util.ExcelUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/material")
@RestController
public class MaterialController {
    private boolean excelHandling = false;

    @Resource
    private MaterialService materialService;

    @GetMapping("/list")
    public Res<List<Material>> list(String name) throws JsonProcessingException {
        List<Material> materials;
        if (StringUtils.hasText(name)) {
            materials = materialService.findAllByNameLike(name);
        } else {
            materials = materialService.findAll();
        }
        return new Res<List<Material>>().success(materials);
    }

    @GetMapping("/template")
    public void template(HttpServletResponse response) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("物料库存模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流

            XSSFSheet category1 = wb.createSheet("物料分类1");
            XSSFSheet category2 = wb.createSheet("物料分类2");
            ExcelUtil.createMaterialSheet(category1, List.of(
                    new Material("NO_1","物料1", "物料品类1", 32L, 10L),
                    new Material("NO_2","物料2", "物料品类1", 21L, 10L),
                    new Material("NO_3","物料3", "物料品类1", 11L, 10L),
                    new Material("NO_4","物料4", "物料品类1", 111L, 10L)
            ));
            ExcelUtil.createMaterialSheet(category2, List.of(
                    new Material("NO_A_1","物料1", "物料品类2", 32L, 10L)
            ));
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(map.get("message"));
        }
    }

    @PutMapping
    public Res<Material> update(@RequestBody Material material) throws Exception {
        materialService.update(material);
        return new Res<Material>().success(material);
    }

    @DeleteMapping("/{materialId}")
    public Res<String> deleteMaterial(@PathVariable Long materialId) {
        materialService.delete(materialId);
        return new Res<String>().success("success");
    }

    @PostMapping
    public Res<Material> add(@RequestBody Material material) throws Exception {
        materialService.add(material);
        return new Res<Material>().success(material);
    }

    @PostMapping("/batch")
    public Res<String> batchAdd(MultipartFile file) {
        if (excelHandling) {
            return new Res<String>().fail("目前有别的批量上传进行中，可能是您的同事在操作。请稍后重试");
        } else {
            excelHandling = true;
            try {
                List<Material> materials = ExcelUtil.getMaterials(file.getInputStream());
                materialService.fullUpdate(materials);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                excelHandling = false;
            }
            return new Res<String>().success("success");
        }
    }
}

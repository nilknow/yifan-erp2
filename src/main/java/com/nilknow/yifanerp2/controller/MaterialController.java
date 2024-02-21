package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.service.MaterialService;
import com.nilknow.yifanerp2.util.ExcelUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/material")
@Slf4j
public class MaterialController {
    private boolean excelHandling = false;

    @Resource
    private MaterialService materialService;

    @PostMapping("/do-remove/{id}")
    @ResponseBody
    public String doRemove(@PathVariable("id") Long id) {
        materialService.delete(id);
        return "ok";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("material", new Material());
        return "page/material/add";
    }

    @PostMapping("/remove-all")
    public String removeAll() {
        materialService.removeAll();
        return "redirect:list";
    }

    @PostMapping("/excel/add")
    public synchronized String excelAdd(MultipartFile file) {
        if (excelHandling) {
            return "redirect:/page/error";
        } else {
            excelHandling = true;
            try {
                List<Material> materials = ExcelUtil.getMaterials(file.getInputStream());
                materialService.saveAll(materials);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                excelHandling = false;
            }
            return "redirect:/material/list";
        }
    }

    /**
     * 下载excel模板
     */
    @GetMapping("/excel/template")
    public void excelTemplate(HttpServletResponse response) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("物料库存模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流

            XSSFSheet category1 = wb.createSheet("物料分类1");
            XSSFSheet category2 = wb.createSheet("物料分类2");
            ExcelUtil.createMaterialSheet(category1, List.of(
                    new Material("物料1", "物料品类1", 32L, 10L),
                    new Material("物料2", "物料品类1", 21L, 10L),
                    new Material("物料3", "物料品类1", 11L, 10L),
                    new Material("物料4", "物料品类1", 111L, 10L)
            ));
            ExcelUtil.createMaterialSheet(category2, List.of(
                    new Material("物料1", "物料品类2", 32L, 10L)
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

    @GetMapping("/excel/download")
    public void excelDownload(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("物料库存", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流

            List<Material> materials = materialService.findAll();
            ExcelUtil.exportMaterials(response.getOutputStream(), materials);
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

    @GetMapping("/update")
    public String update(@RequestParam("id") Long id, Model model) {
        Optional<Material> materialOpt=materialService.findById(id);
        model.addAttribute("material",materialOpt.orElse(new Material()));
        return "page/material/update";
    }

    @PostMapping("do-update")
    public String doUpdate(@ModelAttribute Material material){
        if (materialService.findById(material.getId()).isEmpty()) {
            log.error("ID不正确，无法修改该物料");
            return "redirect:list";
        }
        materialService.save(material);
        return "redirect:list";
    }

    @PostMapping("/do-add")
    public String doAll(@ModelAttribute Material material) {
        materialService.add(material);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Material> materials = materialService.findAll();
        model.addAttribute("materials", materials);
        return "page/material/list";
    }
}

package com.nilknow.yifanerp2.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.entity.Product;
import com.nilknow.yifanerp2.entity.excel.MaterialExcelTemplate;
import com.nilknow.yifanerp2.entity.excel.ProductExcelTemplate;
import com.nilknow.yifanerp2.listener.MaterialUploadListener;
import com.nilknow.yifanerp2.service.MaterialService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    @Resource
    private MaterialUploadListener materialUploadListener;

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
    public synchronized String excelAdd(MultipartFile file) throws IOException {
        if (excelHandling) {
            return "redirect:/page/error";
        } else {
            excelHandling = true;
            try {
                EasyExcel.read(file.getInputStream(), MaterialExcelTemplate.class, materialUploadListener).sheet().doRead();
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
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // todo update filename
            String fileName = URLEncoder.encode("物料库存模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), MaterialExcelTemplate.class)
                    .autoCloseStream(Boolean.FALSE).sheet("物料品类1")
                    .doWrite(
                            List.of(
                                    new MaterialExcelTemplate("物料1","物料品类1", 32L,10L),
                                    new MaterialExcelTemplate("物料2","物料品类1", 21L, 10L),
                                    new MaterialExcelTemplate("物料3","物料品类1", 11L, 10L),
                                    new MaterialExcelTemplate("物料4","物料品类1", 111L, 10L)
                            )
                    );
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
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
            // todo update filename
            String fileName = URLEncoder.encode("物料库存", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), Material.class).autoCloseStream(Boolean.FALSE).sheet("物料库存")
                    .doWrite(materialService.findAll());
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
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

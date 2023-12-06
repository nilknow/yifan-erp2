package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.service.MaterialService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/material")
@Slf4j
public class MaterialController {
    @Resource
    private MaterialService materialService;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("material", new Material());
        return "page/material/add";
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

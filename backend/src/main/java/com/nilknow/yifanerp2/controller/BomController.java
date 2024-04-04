package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.entity.Product;
import com.nilknow.yifanerp2.entity.ProductMaterialRel;
import com.nilknow.yifanerp2.service.MaterialService;
import com.nilknow.yifanerp2.service.ProductMaterialRelService;
import com.nilknow.yifanerp2.service.ProductService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bom")
public class BomController {
    @Resource
    private ProductService productService;
    @Resource
    private ProductMaterialRelService productMaterialRelService;
    @Resource
    private MaterialService materialService;

    @GetMapping
    public String index(Model model) {
        List<Product> products = productService.findAllOrderByUpdateTimeDesc();
        model.addAttribute("products", products);
        if (products.isEmpty()) {
            model.addAttribute("prompt", "您必须首先创建一个产品才能够进行bom管理");
        }
        return "page/bom/index";
    }

    @GetMapping("/list")
    public List<Product> list(String name) {
        if (StringUtils.hasText(name)) {
            return productService.findAllByNameContainingIgnoreCaseOrderByUpdateTimestampDesc(name);
        }
        return productService.findAllOrderByUpdateTimeDesc();
    }

    @GetMapping("/bom/example")
    public String example() {
        return "page/bom/example";
    }

    @GetMapping("/bom/{productId}")
    @ResponseBody
    public List<ProductMaterialRel> materials(@PathVariable("productId") Long productId, Model model) {
        return productMaterialRelService.findAllByProductId(productId);
    }

    @PostMapping("/add")
    @ResponseBody
    public Res<List<ProductMaterialRel>> add(@NotNull @RequestBody ProductMaterialRelDto dto) {
        try {
            productMaterialRelService.add(dto.getProductId(), dto.getMaterialId(), dto.getMaterialCount());
        } catch (Exception e) {
            return new Res<List<ProductMaterialRel>>()
                    .fail(e.getMessage());
        }
        return new Res<List<ProductMaterialRel>>()
                .success(productMaterialRelService.findAllByProductId(dto.getProductId()));
    }

    @Data
    public static class ProductMaterialRelDto {
        private Long productId;
        private Long materialId;
        private Long materialCount;
    }

    @GetMapping("/info")
    @ResponseBody
    public List<ProductMaterialRel> info(@NotNull Long productId) {
        return productMaterialRelService.findAllByProductId(productId);
    }

    @GetMapping("/{productId}")
    public String materialsDetail(Long productId, Model model) {
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty()) {
            return "redirect:/page/bom/error/product-not-exist";
        }
        List<ProductMaterialRel> rels = productMaterialRelService.findAllByProductId(productId);
        model.addAttribute("product", product.get());
        model.addAttribute("MaterialCategories", materialService.findCategories());
        model.addAttribute("allMaterials", materialService.findAll());
        model.addAttribute("rels", rels);
        return "page/bom/update";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") Long id, Model model) {
        Optional<Product> product = productService.findById(id);
        if (product.isEmpty()) {
            return "redirect:/page/bom/error/product-not-exist";
        }
        List<ProductMaterialRel> rels = productMaterialRelService.findAllByProductId(id);
        model.addAttribute("product", product.get());
        model.addAttribute("MaterialCategories", materialService.findCategories());
        model.addAttribute("allMaterials", materialService.findAll());
        model.addAttribute("rels", rels);
        return "page/bom/update";
    }

    @PostMapping("/material/add")
    public String add(@RequestParam("productId") Long productId,
                      @RequestParam("materialId") Long materialId,
                      @RequestParam("materialCount") Long materialCount) throws Exception {
        productMaterialRelService.add(productId, materialId, materialCount);
        return "redirect:/bom/update?id=" + productId;
    }

    @PostMapping("/material/remove")
    public String remove(@RequestParam("productId") Long productId,
                         @RequestParam("materialId") Long materialId) {
        productMaterialRelService.remove(productId, materialId);
        return "redirect:/bom/update?id=" + productId;
    }

    @PostMapping("/material/update-count")
    public String update(@RequestParam("productId") Long productId,
                         @RequestParam("materialId") Long materialId,
                         @RequestParam("materialCount") Long materialCount) {
        productMaterialRelService.updateCount(productId, materialId, materialCount);
        return "redirect:/bom/update?id=" + productId;
    }

    @DeleteMapping("/delete")
    public Res<List<Product>> delete(@RequestParam("productId") Long productId) {
        productMaterialRelService.deleteByProductId(productId);
        return new Res<List<Product>>()
                .success(productService.findAllOrderByUpdateTimeDesc());
    }

    @DeleteMapping("/rel")
    public Res<String> relDelete(@RequestParam("id") Long relId) {
        productMaterialRelService.deleteById(relId);
        return new Res<String>().success("success");
    }
}

package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.dto.ProductDto;
import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.entity.Product;
import com.nilknow.yifanerp2.entity.ProductPlan;
import com.nilknow.yifanerp2.entity.excel.ProductExcelTemplate;
import com.nilknow.yifanerp2.exception.ResException;
import com.nilknow.yifanerp2.repository.CategoryRepository;
import com.nilknow.yifanerp2.repository.ProductPlanRepository;
import com.nilknow.yifanerp2.repository.ProductRepository;
import com.nilknow.yifanerp2.service.ProductService;
import com.nilknow.yifanerp2.util.ExcelUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private boolean excelHandling = false;
    @Resource
    private ProductService productService;
    private final ProductRepository productRepository;
    @Resource
    private ProductPlanRepository productPlanRepository;
    @Resource
    private CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("product", new Product());
        return "page/product/add";
    }

    @PostMapping
    public Res<List<Product>> create(@RequestBody ProductDto product) throws ResException {
        productService.create(product);
        return new Res<List<Product>>().success(productService.findAll());
    }

    @DeleteMapping("/{productId}")
    public Res<String> create(@PathVariable Long productId) {
        productService.delete(productId);
        return new Res<String>().success("success");
    }

    @PutMapping
    public Res<Product> update(@RequestBody ProductDto productDto) throws Exception{
        Product product=productService.update(productDto);
        return new Res<Product>().success(product);
    }

    @GetMapping("/add/plan")
    public String addPlan(Model model) {
        model.addAttribute("productPlan", new ProductPlan());
        return "page/product/add-plan";
    }

    @GetMapping("/do/add-plan")
    public String doAddPlan(@ModelAttribute ProductPlan productPlan) {
        Optional<Product> productOpt = productRepository.findByName(productPlan.getProduct().getName());
        if (productOpt.isEmpty()) {
            return "/page/product/error/no-such-product.html";
        }
        productPlan.setProduct(productOpt.get());
        productPlanRepository.save(productPlan);
        return "redirect:add-plan";
    }

    @PostMapping("/do/add")
    public String doAdd(@ModelAttribute Product p) {
        productService.add(p);
        return "redirect:list";
    }

    @PostMapping("produce")
    public String produce(@RequestParam("id") Long id, @RequestParam("count") Long count) {
        List<Material> notEnoughMaterials = productService.produce(id, count);
        if (!notEnoughMaterials.isEmpty()) {
            return "/page/product/error/material-not-enough";
        }
        return "redirect:list";
    }

    @PostMapping("/out")
    public String out(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "count", required = true) Long count) {
        if (count <= 0) {
            return "redirect:list";
        }
        boolean canOut = productService.checkOut(id, count);
        if (!canOut) {
            return "/page/product/error/product-count-not-enough";
        } else {
            productService.out(id, count);
        }
        return "redirect:list";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long productId) {
        if (productId != null && productId > 0) {
            productService.remove(productId);
        }
        return "redirect:list";
    }

    @PostMapping("/remove-all")
    public String removeAll() {
        productService.removeAll();
        return "redirect:list";
    }

    @PostMapping("/excel/add")
    public synchronized String excelAdd(MultipartFile file) {
        if (excelHandling) {
            return "/page/product/error/excel_style_not_correct";
        } else {
            excelHandling = true;
            try {
                InputStream is = file.getInputStream();
                List<Product> products = ExcelUtil.getProducts(is, new HashSet<>(categoryRepository.findAll()));
                productService.saveAll(products);
            } catch (Exception e) {
                return "/page/product/error/excel_style_not_correct";
            } finally {
                excelHandling = false;
            }
            return "redirect:list";
        }
    }

    /**
     * 下载excel模板
     */
    @GetMapping("/excel/template")
    public void excelTemplate(HttpServletResponse response) throws IOException {
        String fileName = URLEncoder.encode("成品库存模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            //get output stream
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            OutputStream os = response.getOutputStream();

            List<ProductExcelTemplate> householdItemList = List.of(
                    new ProductExcelTemplate("桌子", 32L),
                    new ProductExcelTemplate("椅子", 21L));
            List<ProductExcelTemplate> stationeryList = List.of(
                    new ProductExcelTemplate("书包", 32L),
                    new ProductExcelTemplate("铅笔", 21L));

            XSSFSheet householdItemSheet = workbook.createSheet("家居用品");
            XSSFSheet stationerySheet = workbook.createSheet("文具");
            ExcelUtil.createProductSheet(householdItemSheet, householdItemList);
            ExcelUtil.createProductSheet(stationerySheet, stationeryList);
            workbook.write(os);
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
            // todo update filename
            String fileName = URLEncoder.encode("成品库存", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            List<Product> products = productService.findAll();
            ExcelUtil.exportProducts(response.getOutputStream(), products);
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

    @GetMapping("/list")
    public Res<List<Product>> list(@RequestParam(required = false) String name) {
        if (StringUtils.hasText(name)) {
            return new Res<List<Product>>()
                    .success(productService.findAllByNameContainingIgnoreCaseOrderByUpdateTimestampDesc(name));
        } else {
            return new Res<List<Product>>().success(productService.findAll());
        }
    }

    @GetMapping("/plan")
    public String plan(Model model) {
        List<ProductPlan> productPlans = productPlanRepository.findAll();
        model.addAttribute("productPlans", productPlans);
        return "page/product/plan";
    }

}

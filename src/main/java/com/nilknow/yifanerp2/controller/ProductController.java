package com.nilknow.yifanerp2.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.nilknow.yifanerp2.entity.Product;
import com.nilknow.yifanerp2.entity.excel.ProductExcelTemplate;
import com.nilknow.yifanerp2.listener.ProductUploadListener;
import com.nilknow.yifanerp2.service.ProductService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {
    private boolean excelHandling = false;
    @Resource
    private ProductService productService;
    @Resource
    private ProductUploadListener uploadListener;

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("product", new Product());
        return "page/product/add";
    }

    @PostMapping("/remove-all")
    public String removeAll() {
        productService.removeAll();
        return "redirect:list";
    }

    @PostMapping("/excel/add")
    public synchronized String excelAdd(MultipartFile file) throws IOException {
        if (excelHandling) {
            return "redirect:/page/error";
        } else {
            excelHandling = true;
            try {
                EasyExcel.read(file.getInputStream(), ProductExcelTemplate.class, uploadListener).sheet().doRead();
            } finally {
                excelHandling = false;
            }
            return "redirect:/product/list";
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
            String fileName = URLEncoder.encode("产品库存模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), ProductExcelTemplate.class)
                    .autoCloseStream(Boolean.FALSE).sheet("产品库存模板")
                    .doWrite(
                            List.of(
                                    new ProductExcelTemplate("产品1", 32L),
                                    new ProductExcelTemplate("产品2", 21L),
                                    new ProductExcelTemplate("产品3", 11L)
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
            String fileName = URLEncoder.encode("产品库存", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), Product.class).autoCloseStream(Boolean.FALSE).sheet("产品库存")
                    .doWrite(productService.findAll());
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

    @PostMapping("/do-add")
    public String doAdd(@ModelAttribute Product p) {
        productService.add(p);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "page/product/list";
    }
}

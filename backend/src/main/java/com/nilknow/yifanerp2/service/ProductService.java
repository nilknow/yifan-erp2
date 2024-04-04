package com.nilknow.yifanerp2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nilknow.yifanerp2.dto.ProductDto;
import com.nilknow.yifanerp2.entity.Category;
import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.entity.Product;
import com.nilknow.yifanerp2.entity.paging.Page;
import com.nilknow.yifanerp2.entity.paging.Paged;
import com.nilknow.yifanerp2.entity.paging.Paging;
import com.nilknow.yifanerp2.exception.ResException;
import com.nilknow.yifanerp2.repository.CategoryRepository;
import com.nilknow.yifanerp2.repository.ProductRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ProductService {
    @Resource
    private ProductRepository productRepository;
    @Resource
    private ProductMaterialRelService productMaterialRelService;
    @Resource
    private CategoryRepository categoryRepository;

    public Paged<Product> getProducts(int pageNumber, int size) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Product> Products = objectMapper.readValue(getClass().getClassLoader()
                        .getResourceAsStream("Products.json"),
                new TypeReference<List<Product>>() {
                });

        List<Product> paged = Products.stream()
                .skip(pageNumber)
                .limit(size)
                .collect(Collectors.toList());

        int totalPages = Products.size() / size;
        return new Paged<>(new Page<>(paged, totalPages), Paging.of(totalPages, pageNumber, size));
    }

    @Transactional
    public void add(Product p) {
        try {
            productRepository.save(p);
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    public List<Product> findAll() {
        return productRepository.findAllByOrderByUpdateTimestampDesc();
    }

    public void saveAll(List<Product> cachedDataList) {
        productRepository.saveAll(cachedDataList);
    }

    public void removeAll() {
        productRepository.deleteAll();
    }

    public void remove(Long productId) {
        productRepository.deleteById(productId);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public List<Material> produce(Long id, Long count) {
        Optional<Product> p = productRepository.findById(id);
        if (p.isEmpty()) {
            //todo error
            return new ArrayList<>();
        }
        List<Material> notEnoughMaterials = productMaterialRelService.canProduce(id, count);
        if (notEnoughMaterials.isEmpty()) {
            produce(p.get(), count);
            productMaterialRelService.produce(id, count);
        }
        return notEnoughMaterials;
    }

    private void produce(Product product, Long count) {
        product.setCount(product.getCount() + count);
        productRepository.save(product);
    }

    public List<Product> findAllOrderByUpdateTimeDesc() {
        return productRepository.findAllByOrderByUpdateTimestampDesc();
    }

    public boolean checkOut(Long id, Long count) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            return false;
        }
        Product product = productOpt.get();
        return product.getCount() != null && product.getCount() >= count;
    }

    public void out(Long id, Long count) {
        if (count <= 0) {
            return;
        }
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            return;
        }
        Product product = productOpt.get();
        product.setCount(product.getCount() - count);
        productRepository.save(product);
    }

    public List<Product> findAllByNameContainingIgnoreCaseOrderByUpdateTimestampDesc(String name) {
        return productRepository.findAllByNameContainingIgnoreCaseOrderByUpdateTimestampDesc(name);
    }

    public void create(ProductDto product) throws ResException {
        if (!StringUtils.hasText(product.getSerialNum())) {
            throw new ResException("product must have serial number");
        }
        if (!StringUtils.hasText(product.getName())) {
            throw new ResException("product must have name");
        }
        if (!StringUtils.hasText(product.getCategoryName())) {
            throw new ResException("product must have category");
        }
        if (!StringUtils.hasText(product.getUnit())) {
            throw new ResException("product must have unit");
        }

        List<Product> products = productRepository.findAllBySerialNum(product.getSerialNum());
        if (!CollectionUtils.isEmpty(products)) {
            throw new ResException("product with same serial number already exists");
        }
        Category category;
        List<Category> categories = categoryRepository.findAllByName(product.getCategoryName());
        if (CollectionUtils.isEmpty(categories)) {
            Category newCategory = new Category(null, product.getCategoryName(), null);
            categoryRepository.save(newCategory);
            category = newCategory;
        } else {
            category = categories.get(0);
        }
        List<Product> productsSameName = productRepository
                .findAllByNameAndCategory(product.getName(), category);
        if (!CollectionUtils.isEmpty(productsSameName)) {
            throw new ResException("product with same name and category already exists");
        }

        Product p = new Product();
        p.setUpdateTimestamp(new Date());
        p.setCategory(category);
        p.setSerialNum(product.getSerialNum());
        p.setDescription(product.getDescription());
        p.setUnit(product.getUnit());
        p.setCount(product.getCount());
        p.setName(product.getName());
        productRepository.save(p);
    }

    @Transactional
    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }

    @Transactional
    public Product update(ProductDto dto) throws ResException {
        if(dto.getId() == null){
            throw new ResException("product id must not be null");
        }
        if (dto.getSerialNum() == null) {
            throw new ResException("product serial number must not be null");
        }
        if(dto.getName() == null){
            throw new ResException("product name must not be null");
        }
        if (dto.getCategoryName() == null) {
            throw new ResException("product category name must not be null");
        }
        if (dto.getUnit() == null) {
            throw new ResException("product unit must not be null");
        }

        Product p = productRepository.findById(dto.getId())
                .orElseThrow(() -> new ResException("product not found"));
        if (!dto.getSerialNum().equals(p.getSerialNum())) {
            List<Product> allBySerialNum = productRepository.findAllBySerialNum(dto.getSerialNum());
            if (!CollectionUtils.isEmpty(allBySerialNum)) {
                throw new ResException("product with same serial number already exists");
            }
        }
        List<Category> categories = categoryRepository.findAllByName(dto.getCategoryName());
        Category category;
        if(CollectionUtils.isEmpty(categories)){
            category = new Category(null, dto.getCategoryName(), null);
            categoryRepository.save(category);
        }else {
            category = categories.get(0);
        }
        if((!dto.getName().equals(p.getName())
        || (!dto.getCategoryName().equals(p.getCategory().getName())))){
            List<Product> productsSameName = productRepository
                    .findAllByNameAndCategory(dto.getName(), category);
            if (!CollectionUtils.isEmpty(productsSameName)) {
                throw new ResException("product with same name and category already exists");
            }
        }

        p.setName(dto.getName());
        p.setUpdateTimestamp(new Date());
        p.setCategory(category);
        p.setDescription(dto.getDescription());
        p.setUnit(dto.getUnit());
        p.setSerialNum(dto.getSerialNum());
        p.setCount(dto.getCount());
        productRepository.save(p);
        return p;
    }
}

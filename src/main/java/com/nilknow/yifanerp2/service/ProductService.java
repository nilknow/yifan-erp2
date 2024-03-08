package com.nilknow.yifanerp2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.entity.Product;
import com.nilknow.yifanerp2.entity.paging.Page;
import com.nilknow.yifanerp2.entity.paging.Paged;
import com.nilknow.yifanerp2.entity.paging.Paging;
import com.nilknow.yifanerp2.repository.ProductRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    @Resource
    private ProductRepository productRepository;
    @Resource
    private ProductMaterialRelService productMaterialRelService;

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
        }catch (Exception e){
            log.error("error",e);
        }
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void saveAll(List<Product> cachedDataList) {
        productRepository.saveAll(cachedDataList);
    }

    public void removeAll() {
        productRepository.deleteAll();
    }

    public List<Material> findMaterialsByProductId(Long productId){
        return productRepository.findMaterialsByProductId(productId);
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
            productMaterialRelService.produce(id,count);
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
        product.setCount(product.getCount()-count);
        productRepository.save(product);
    }

}

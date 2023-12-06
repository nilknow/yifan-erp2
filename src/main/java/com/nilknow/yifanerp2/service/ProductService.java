package com.nilknow.yifanerp2.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nilknow.yifanerp2.entity.Product;
import com.nilknow.yifanerp2.entity.paging.Page;
import com.nilknow.yifanerp2.entity.paging.Paged;
import com.nilknow.yifanerp2.entity.paging.Paging;
import com.nilknow.yifanerp2.repository.ProductRepository;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    @Resource
    private ProductRepository productRepository;

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
        productRepository.save(p);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}

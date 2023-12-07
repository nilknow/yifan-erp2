package com.nilknow.yifanerp2.service;

import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.entity.Product;
import com.nilknow.yifanerp2.entity.ProductMaterialRel;
import com.nilknow.yifanerp2.repository.MaterialRepository;
import com.nilknow.yifanerp2.repository.ProductMaterialRelRepository;
import com.nilknow.yifanerp2.repository.ProductRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductMaterialRelService {
    @Resource
    private ProductMaterialRelRepository productMaterialRelRepository;
    @Resource
    private ProductRepository productRepository;
    @Resource
    private MaterialRepository materialRepository;

    public List<ProductMaterialRel> findAllByProductId(Long productId) {
        return productMaterialRelRepository.findAllByProductId(productId);
    }

    @Transactional
    public void add(Long productId, Long materialId, Long materialCount) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            //todo throw error
            return;
        }
        Optional<Material> material = materialRepository.findById(materialId);
        if (material.isEmpty()) {
            //todo throw error
            return;
        }

        ProductMaterialRel productMaterialRel = new ProductMaterialRel();
        productMaterialRel.setProduct(product.get());
        productMaterialRel.setMaterial(material.get());
        productMaterialRel.setMaterialCount(materialCount);
        productMaterialRelRepository.save(productMaterialRel);
    }

    @Transactional
    public void remove(Long productId, Long materialId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            //todo throw error
            return;
        }
        Optional<Material> material = materialRepository.findById(materialId);
        if (material.isEmpty()) {
            //todo throw error
            return;
        }

        ProductMaterialRel rel = new ProductMaterialRel();
        rel.setMaterial(material.get());
        rel.setProduct(product.get());
        List<ProductMaterialRel> rels = productMaterialRelRepository.findAll(Example.of(rel));
        if (rels.size() != 1) {
            return;
        }

        productMaterialRelRepository.deleteById(rels.get(0).getId());
    }

    @Transactional
    public void updateCount(Long productId, Long materialId, Long count) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            //todo throw error
            return;
        }
        Optional<Material> material = materialRepository.findById(materialId);
        if (material.isEmpty()) {
            //todo throw error
            return;
        }

        ProductMaterialRel rel = new ProductMaterialRel();
        rel.setMaterial(material.get());
        rel.setProduct(product.get());
        List<ProductMaterialRel> rels = productMaterialRelRepository.findAll(Example.of(rel));
        if (rels.size() != 1) {
            return;
        }

        ProductMaterialRel newRel = rels.get(0);
        newRel.setMaterialCount(count);
        productMaterialRelRepository.save(newRel);
    }

    public List<Material> canProduce(Long productId, Long count) {
        List<Material> countNotEnoughMaterials = new ArrayList<>();
        List<ProductMaterialRel> rels = productMaterialRelRepository.findAllByProductId(productId);
        for (ProductMaterialRel rel : rels) {
            Long materialNeed = rel.getMaterialCount() * count;
            Long materialHave = rel.getMaterial().getCount();
            if (materialHave < materialNeed) {
                countNotEnoughMaterials.add(rel.getMaterial());
            }
        }
        return countNotEnoughMaterials;
    }

    @Transactional
    public void produce(Long productId, Long count) {
        List<ProductMaterialRel> rels = productMaterialRelRepository.findAllByProductId(productId);
        for (ProductMaterialRel rel : rels) {
            Long materialNeed = rel.getMaterialCount() * count;
            Long materialHave = rel.getMaterial().getCount();
            rel.getMaterial().setCount(materialHave - materialNeed);
        }
    }
}

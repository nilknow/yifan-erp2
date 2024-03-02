package com.nilknow.yifanerp2.service;

import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.repository.MaterialRepository;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {
    @Resource
    private MaterialRepository materialRepository;

    public List<Material> findAll() {
        return materialRepository.findAll();
    }

    @Transactional
    public void add(Material material) {
        materialRepository.save(material);
    }

    public Optional<Material> findById(Long id) {
        return materialRepository.findById(id);
    }

    public void save(Material material) {
        materialRepository.save(material);
    }

    public void saveAll(List<Material> list) {
        materialRepository.saveAll(list);
    }

    public void removeAll() {
        materialRepository.deleteAll();
    }

    public void delete(Long id) {
        materialRepository.deleteById(id);
    }

    public List<String> findCategories(){
        return materialRepository.findDistinctCategories();
    }
}

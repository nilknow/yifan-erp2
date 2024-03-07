package com.nilknow.yifanerp2.service;

import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.repository.MaterialRepository;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MaterialService {
    private static final String TO_ADD_KEY = "toAdd";
    private static final String TO_DELETE_KEY = "toDelete";
    private static final String TO_UPDATE_KEY = "toUpdate";
    @Resource
    private MaterialRepository materialRepository;

    public List<Material> findAll() {
        return materialRepository.findAllByOrderByUpdateTimestampDesc();
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

    /**
     * @see MaterialService#fullUpdate(List) preview for full update
     */
    public Map<String,List<Material>> fullUpdatePreview(List<Material> newList){
        Map<String, List<Material>> preview=new HashMap<>();
        List<Material> list = materialRepository.findAll();
        Map<String, Material> map = list.stream().collect(Collectors.toMap(
                x -> x.getName() + "_" + x.getCategory(),
                x -> x
        ));
        Map<String, Material> newMap = newList.stream().collect(Collectors.toMap(
                x -> x.getName() + "_" + x.getCategory(),
                x -> x
        ));

        List<Material> toAdd = new ArrayList<>();
        for (Map.Entry<String, Material> e : newMap.entrySet()) {
            if (!map.containsKey(e.getKey())) {
                toAdd.add(e.getValue());
            }
        }
        List<Material> toRemove = new ArrayList<>();
        for (Map.Entry<String, Material> e : map.entrySet()) {
            if (!newMap.containsKey(e.getKey())) {
                toRemove.add(e.getValue());
            }
        }
        List<Material> toUpdate = new ArrayList<>();
        for (Material m : list) {
            for (Material newM : newList) {
                if (m.getName().equals(newM.getName()) && m.getCategory().equals(newM.getCategory())) {
                    if (!Objects.equals(m.getCount(), newM.getCount()) || !Objects.equals(m.getInventoryCountAlert(), newM.getInventoryCountAlert())) {
                        toUpdate.add(newM);
                    }
                }
            }
        }

        preview.put(TO_ADD_KEY, toAdd);
        preview.put(TO_DELETE_KEY, toRemove);
        preview.put(TO_UPDATE_KEY, toUpdate);
        return preview;
    }

    /**
     * @see MaterialService#fullUpdatePreview(List) better to preview before full update
     */
    @Transactional
    public void fullUpdate(List<Material> newList) {
        Map<String, List<Material>> preview = fullUpdatePreview(newList);

        materialRepository.saveAll(preview.get(TO_ADD_KEY));
        materialRepository.deleteAll(preview.get(TO_DELETE_KEY));
        this.updateAllByNameAndCategory(preview.get(TO_UPDATE_KEY));
    }

    private void updateAllByNameAndCategory(List<Material> materials){
        for (Material material : materials) {
            materialRepository.updateByNameAndCategory(material.getCount(),material.getInventoryCountAlert(),material.getName(),material.getCategory());
        }
    }

    public void removeAll() {
        materialRepository.deleteAll();
    }

    @Transactional
    public void delete(Long id) {
        materialRepository.deleteById(id);
    }

    public List<String> findCategories() {
        return materialRepository.findDistinctCategories();
    }

    public List<Material> findAllByNameLike(String name) {
        return materialRepository.findAllByNameContainingIgnoreCase(name);
    }

    public List<Material> findAllByCategory(String category) {
        return materialRepository.findALlByCategory(category);
    }
}

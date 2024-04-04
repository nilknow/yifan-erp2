package com.nilknow.yifanerp2.service;

import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.exception.ResException;
import com.nilknow.yifanerp2.repository.MaterialRepository;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public void add(Material material) throws ResException {
        if (!StringUtils.hasText(material.getSerialNum())) {
            throw new ResException("serialNum shouldn't be empty");
        }
        if (!StringUtils.hasText(material.getName())) {
            throw new ResException("name shouldn't be empty");
        }
        if (!StringUtils.hasText(material.getCategory())) {
            throw new ResException("category shouldn't be empty");
        }
        if(material.getCount() == null) {
            throw new ResException("count shouldn't be null");
        }
        if(material.getInventoryCountAlert() == null) {
            throw new ResException("inventoryCountAlert shouldn't be null");
        }
        boolean sameSerialNum=materialRepository.existsBySerialNum(material.getSerialNum());
        if (sameSerialNum) {
            throw new ResException("serialNum already exists");
        }
        boolean sameNameSameCategory=materialRepository.existsByNameAndCategory(material.getName(), material.getCategory());
        if (sameNameSameCategory) {
            throw new ResException("same name already exists in this category");
        }
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
    public Map<String, List<Material>> fullUpdatePreview(List<Material> newList) {
        Map<String, List<Material>> preview = new HashMap<>();
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

    private void updateAllByNameAndCategory(List<Material> materials) {
        for (Material material : materials) {
            materialRepository.updateByNameAndCategory(material.getCount(), material.getInventoryCountAlert(), material.getName(), material.getCategory());
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
        return materialRepository.findAllByNameContainingIgnoreCaseOrderByUpdateTimestampDesc(name);
    }

    public List<Material> findAllByCategory(String category) {
        return materialRepository.findAllByCategory(category);
    }

    public void update(Material material) throws Exception {
        sameSerialNumCheck(material);
        sameNameAndCategoryCheck(material);
        materialRepository.save(material);
    }

    private void sameSerialNumCheck(Material material) throws Exception {
        List<Material> materials = materialRepository.findAllBySerialNum(material.getSerialNum());
        if (materials.size() > 1) {
            throw new ResException("已经有重复的物料编号且数据库中有雷同数据");
        }
        if (!materials.get(0).getId().equals(material.getId())) {
            throw new ResException("已经有重复的物料编号");
        }
    }

    private void sameNameAndCategoryCheck(Material material) throws Exception {
        List<Material> materials = materialRepository.findAllByNameAndCategory(material.getName(), material.getCategory());
        if (materials.size() > 1) {
            throw new ResException("分类下已经有重复的物料名且数据库中有雷同数据");
        }
        if (!materials.get(0).getId().equals(material.getId())) {
            throw new ResException("分类下已经有重复的物料名");
        }
    }
}

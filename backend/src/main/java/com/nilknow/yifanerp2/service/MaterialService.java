package com.nilknow.yifanerp2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nilknow.yifanerp2.config.security.UserIdHolder;
import com.nilknow.yifanerp2.entity.ActionLog;
import com.nilknow.yifanerp2.entity.LoginUser;
import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.exception.ResException;
import com.nilknow.yifanerp2.repository.LoginUserRepository;
import com.nilknow.yifanerp2.repository.MaterialRepository;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MaterialService {
    private static final String TO_ADD_KEY = "toAdd";
    private static final String TO_DELETE_KEY = "toDelete";
    private static final String TO_UPDATE_KEY = "toUpdate";
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private MaterialRepository materialRepository;
    @Resource
    private ActionLogService actionLogService;
    @Resource
    private LoginUserRepository loginUserRepository;

    public List<Material> findAll() {
        return materialRepository.findAllByOrderByUpdateTimestampDesc();
    }

    @Transactional
    public void save(Material material, String source) throws ResException, JsonProcessingException {
        saveValidate(material);

        Material oldMaterial = objectMapper.readValue(objectMapper.writeValueAsString(material), Material.class);
        materialRepository.save(material);

        saveActionLog(material, source, oldMaterial);
    }

    private void saveActionLog(Material material, String source, Material oldMaterial) throws JsonProcessingException {
        ActionLog actionLog;
        if (material.getId() == null) {
            actionLog = ActionLog.add(
                    "material",
                    source,
                    material,
                    "添加新的物料" + material.getName()
                            + " " + material.getCount() + " " + material.getCategory(),
                    loginUserRepository.findById(UserIdHolder.get()).get()
            );
        } else {
            HashMap<String, Material> logMap = new HashMap<>();
            logMap.put("old", oldMaterial);
            logMap.put("new", material);

            actionLog = new ActionLog(
                    UUID.randomUUID(),
                    "update",
                    new Date(),
                    loginUserRepository.findById(UserIdHolder.get()).get(),
                    "material",
                    source,
                    objectMapper.writeValueAsString(logMap),
                    "修改旧物料" + oldMaterial.getName()
                            + " " + oldMaterial.getCount() + " " + oldMaterial.getCategory()
                            + " 为 " + material.getName()
                            + " " + material.getCount() + " " + material.getCategory()
            );
        }
        actionLogService.save(actionLog);
    }

    private void saveValidate(Material material) throws ResException {
        if (!StringUtils.hasText(material.getSerialNum())) {
            throw new ResException("serialNum shouldn't be empty");
        }
        if (!StringUtils.hasText(material.getName())) {
            throw new ResException("name shouldn't be empty");
        }
        if (!StringUtils.hasText(material.getCategory())) {
            throw new ResException("category shouldn't be empty");
        }
        if (material.getCount() == null) {
            throw new ResException("count shouldn't be null");
        }
        if (material.getInventoryCountAlert() == null) {
            throw new ResException("inventoryCountAlert shouldn't be null");
        }
        boolean sameSerialNum = materialRepository.existsBySerialNum(material.getSerialNum());
        if (sameSerialNum) {
            throw new ResException("serialNum already exists");
        }
        boolean sameNameSameCategory = materialRepository.existsByNameAndCategory(material.getName(), material.getCategory());
        if (sameNameSameCategory) {
            throw new ResException("same name already exists in this category");
        }
    }

    public Optional<Material> findById(Long id) {
        return materialRepository.findById(id);
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

        List<Material> toAdds = preview.get(TO_ADD_KEY);

        LoginUser user = loginUserRepository.findById(UserIdHolder.get()).get();
        Date now = new Date();
        String eventType = "save";
        String source = "batch";
        UUID batchId = UUID.randomUUID();
        List<ActionLog> actionLogs = toAdds.stream().map(x ->
                {
                    try {
                        return new ActionLog()
                                .setUser(user)
                                .setTimestamp(now)
                                .setEventType(eventType)
                                .setSource(source)
                                .setBatchId(batchId)
                                .setDescription("添加新物料 " + x.getSerialNum() + " " + x.getName() + " " + x.getCount() + " " + x.getInventoryCountAlert())
                                .setAdditionalInfo(objectMapper.writeValueAsString(x))
                                .setTableName("material");
                    } catch (JsonProcessingException e) {
                        log.error("无法处理数据为JSON, material " + x);
                        throw new RuntimeException(e);
                    }
                }
        ).toList();
        materialRepository.saveAll(toAdds);
        actionLogService.saveAll(actionLogs);
//        materialRepository.deleteAll(preview.get(TO_DELETE_KEY));
//        this.updateAllByNameAndCategory(preview.get(TO_UPDATE_KEY));
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
    public void delete(Long id, String source) throws JsonProcessingException, ResException {
        Optional<Material> toDelete = materialRepository.findById(id);
        if (toDelete.isEmpty()) {
            throw new ResException("The material you want to delete doesn't really exist");
        }
        materialRepository.deleteById(id);

        ActionLog actionLog = new ActionLog();
        actionLog.setUser(loginUserRepository.findById(UserIdHolder.get()).get());
        actionLog.setTimestamp(new Date());
        actionLog.setEventType("delete");
        actionLog.setSource(source);
        actionLog.setBatchId(UUID.randomUUID());
        actionLog.setDescription("删除物料");
        actionLog.setAdditionalInfo(objectMapper.writeValueAsString(toDelete.get()));
        actionLog.setTableName("material");
        actionLogService.save(actionLog);
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

    public void update(Material material, String source) throws Exception {
        if (material.getId() == null) {
            throw new ResException("id shouldn't be null");
        }
        if (!StringUtils.hasText(material.getSerialNum())) {
            throw new ResException("serialNum shouldn't be empty");
        }
        if (!StringUtils.hasText(material.getName())) {
            throw new ResException("name shouldn't be empty");
        }
        if (!StringUtils.hasText(material.getCategory())) {
            throw new ResException("category shouldn't be empty");
        }

        Optional<Material> realMaterialOpt = materialRepository.findById(material.getId());
        if (realMaterialOpt.isEmpty()) {
            throw new ResException("cannot find the material you want to update");
        }
        Material savedMaterial = realMaterialOpt.get();
        sameSerialNumCheck(material, savedMaterial);
        sameNameAndCategoryCheck(material, savedMaterial);

        ActionLog actionLog = new ActionLog();
        HashMap<String, Material> logMap = new HashMap<>();
        actionLog.setEventType("update");
        logMap.put("old", objectMapper.readValue(objectMapper.writeValueAsString(savedMaterial), Material.class));
        actionLog.setDescription("修改旧物料 "
                + savedMaterial.getSerialNum() + " "
                + savedMaterial.getName() + " "
                + savedMaterial.getCount() + " "
                + savedMaterial.getCategory());

        materialRepository.save(material);
        logMap.put("new", material);
        actionLog.setDescription(actionLog.getDescription() + " 为 "
                + material.getSerialNum() + " "
                + material.getName() + " "
                + material.getCount() + " "
                + material.getCategory());
        actionLog.setAdditionalInfo(objectMapper.writeValueAsString(logMap));
        actionLog.setSource(source);
        actionLog.setUser(loginUserRepository.findById(UserIdHolder.get()).get());
        actionLog.setTableName("material");
        actionLog.setTimestamp(new Date());
        actionLog.setBatchId(UUID.randomUUID());
        actionLogService.save(actionLog);
    }

    private void sameSerialNumCheck(Material material, Material savedMaterial) throws Exception {
        if (material.getSerialNum().equals(savedMaterial.getSerialNum())) {
            return;
        }
        List<Material> materials = materialRepository.findAllBySerialNum(material.getSerialNum());
        if (CollectionUtils.isEmpty(materials)) {
            return;
        }
        if (materials.size() > 1) {
            throw new ResException("已经有重复的物料编号且数据库中有雷同数据");
        }
        if (!materials.get(0).getId().equals(material.getId())) {
            throw new ResException("已经有重复的物料编号");
        }
    }

    private void sameNameAndCategoryCheck(Material material, Material savedMaterial) throws Exception {
        if (material.getName().equals(savedMaterial.getName()) && material.getCount().equals(savedMaterial.getCount()) && material.getCategory().equals(savedMaterial.getCategory())) {
            return;
        }
        List<Material> materials = materialRepository.findAllByNameAndCategory(material.getName(), material.getCategory());
        if (CollectionUtils.isEmpty(materials)) {
            return;
        }
        if (materials.size() > 1) {
            throw new ResException("分类下已经有重复的物料名且数据库中有雷同数据");
        }
        if (!materials.get(0).getId().equals(material.getId())) {
            throw new ResException("分类下已经有重复的物料名");
        }
    }

    public List<String> findDistinctCategories() {
        return materialRepository.findDistinctCategories();
    }
}

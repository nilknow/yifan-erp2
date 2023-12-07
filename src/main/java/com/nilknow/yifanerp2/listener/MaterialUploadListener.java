package com.nilknow.yifanerp2.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.entity.excel.MaterialExcelTemplate;
import com.nilknow.yifanerp2.service.MaterialService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MaterialUploadListener implements ReadListener<MaterialExcelTemplate> {
    private static final int BATCH_COUNT = 5;
    private List<MaterialExcelTemplate> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    @Resource
    private MaterialService materialService;

    @Override
    public void invoke(MaterialExcelTemplate material, AnalysisContext analysisContext) {
        cachedDataList.add(material);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
    }

    private void saveData() {
        materialService.saveAll(cachedDataList.stream().map(x -> {
            Material m = new Material();
            m.setName(x.getName());
            m.setCategory(x.getCategory());
            m.setCount(x.getCount());
            m.setInventoryCountAlert(x.getInventoryCountAlert());
            return m;
        }).toList());
    }
}

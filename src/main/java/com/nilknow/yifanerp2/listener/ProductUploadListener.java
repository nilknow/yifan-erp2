package com.nilknow.yifanerp2.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.nilknow.yifanerp2.entity.Product;
import com.nilknow.yifanerp2.entity.excel.ProductExcelTemplate;
import com.nilknow.yifanerp2.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductUploadListener implements ReadListener<ProductExcelTemplate> {
    private static final int BATCH_COUNT = 5;
    private List<ProductExcelTemplate> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    @Resource
    private ProductService productService;

    @Override
    public void invoke(ProductExcelTemplate product, AnalysisContext analysisContext) {
        cachedDataList.add(product);
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
        productService.saveAll(cachedDataList.stream().map(x -> {
            Product product = new Product();
            product.setName(x.getName());
            product.setCount(x.getCount());
            return product;
        }).toList());
    }
}

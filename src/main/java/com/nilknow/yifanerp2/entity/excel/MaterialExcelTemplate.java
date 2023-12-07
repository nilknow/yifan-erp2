package com.nilknow.yifanerp2.entity.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialExcelTemplate {
    private String name;
    private String category;
    private Long count;
    private Long inventoryCountAlert;
}

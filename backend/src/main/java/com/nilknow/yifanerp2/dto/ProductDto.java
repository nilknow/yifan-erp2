package com.nilknow.yifanerp2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String serialNum;
    private String description;
    private String name;
    private Long count;
    private String unit;
    private String categoryName;
}

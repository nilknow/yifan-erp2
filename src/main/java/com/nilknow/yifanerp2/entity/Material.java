package com.nilknow.yifanerp2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private Long count;
    private Long inventoryCountAlert;
    @ManyToMany(mappedBy = "materials")
    private List<Product> products;

    public Material(String name, String category, Long count, Long inventoryCountAlert) {
        this.name = name;
        this.category = category;
        this.count = count;
        this.inventoryCountAlert = inventoryCountAlert;
    }
}

package com.nilknow.yifanerp2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TenantId;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private Long count;
    private Long inventoryCountAlert;
    @Temporal(TemporalType.TIMESTAMP)
    public Date updateTimestamp;
    @ManyToMany(mappedBy = "materials")
    private List<Product> products;
    @TenantId
    private Long companyId;

    public Material(String name, String category, Long count, Long inventoryCountAlert) {
        this.name = name;
        this.category = category;
        this.count = count;
        this.inventoryCountAlert = inventoryCountAlert;
    }
}

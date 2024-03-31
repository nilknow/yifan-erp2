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
    @TenantId
    private Long companyId;

    public Material(String name, String category, Long count, Long inventoryCountAlert) {
        this.name = name;
        this.category = category;
        this.count = count;
        this.inventoryCountAlert = inventoryCountAlert;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", count=" + count +
                ", inventoryCountAlert=" + inventoryCountAlert +
                ", updateTimestamp=" + updateTimestamp +
                ", companyId=" + companyId +
                '}';
    }
}

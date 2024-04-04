package com.nilknow.yifanerp2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TenantId;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serialNum;
    private String description;
    private String name;
    private Long count;
    private String unit;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private Date updateTimestamp;
    @ManyToMany
    @JoinTable(
            name = "product_material_rel",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private List<Material> materials;
    @TenantId
    private Long companyId;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", unit='" + unit + '\'' +
                ", category=" + category +
                ", updateTimestamp=" + updateTimestamp +
                ", companyId=" + companyId +
                '}';
    }
}

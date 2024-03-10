package com.nilknow.yifanerp2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.TenantId;


@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class ProductPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Long count;
    private String unit;
    @TenantId
    private Long companyId;
}

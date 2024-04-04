package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.ProductMaterialRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductMaterialRelRepository extends JpaRepository<ProductMaterialRel,Long> {
    @Query("SELECT r FROM ProductMaterialRel r WHERE r.product.id = :productId")
    List<ProductMaterialRel> findAllByProductId(@Param("productId") Long productId);

    @Query("SELECT r FROM ProductMaterialRel r WHERE r.product.id = :productId and r.material.id=:materialId")

    List<ProductMaterialRel> findAllByProductIdAndMaterialId(Long productId, Long materialId);

    void deleteByProductId(Long productId);
}

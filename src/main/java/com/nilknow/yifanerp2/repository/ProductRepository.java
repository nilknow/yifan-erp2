package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.Material;
import com.nilknow.yifanerp2.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p.materials FROM Product p WHERE p.id = :productId")
    List<Material> findMaterialsByProductId(@Param("productId") Long productId);
}

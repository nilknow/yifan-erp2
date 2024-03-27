package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.Material;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Query(nativeQuery = true, value = "select distinct category from material")
    List<String> findDistinctCategories();

    List<Material> findAllByNameContainingIgnoreCaseOrderByUpdateTimestampDesc(@NotBlank String name);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE material SET count=:count, inventory_count_alert=:inventoryCountAlert where name = :name and category = :category")
    void updateByNameAndCategory(Long count,Long inventoryCountAlert,String name,String category);

    List<Material> findAllByOrderByUpdateTimestampDesc();

    List<Material> findAllByCategory(String category);
}

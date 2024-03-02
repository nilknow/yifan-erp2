package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MaterialRepository extends JpaRepository<Material, Long> {
    @Query(nativeQuery = true, value = "select distinct category from material")
    List<String> findDistinctCategories();
}

package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByName(String categoryName);
}

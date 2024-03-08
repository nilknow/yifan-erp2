package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuggestionRepository extends JpaRepository<Suggestion,Long> {
    List<Suggestion> findAllByOrderByCreateTimeDesc();
}

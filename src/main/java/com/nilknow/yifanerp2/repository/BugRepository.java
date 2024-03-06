package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.Bug;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BugRepository extends JpaRepository<Bug,Long> {

    List<Bug> findAllByOrderByCreateTimeDesc();
}

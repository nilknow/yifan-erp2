package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o order by o.updateTime desc")
    List<Order> findAllByOrderByUpdateTimeDesc();

    List<Order> findAllBySerialNum(String serialNum);
}
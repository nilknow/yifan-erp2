package com.nilknow.yifanerp2.repository;

import com.nilknow.yifanerp2.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findBySerialNum(String deliverySerialNum);
}

package com.nilknow.yifanerp2.service;

import com.nilknow.yifanerp2.entity.Delivery;
import com.nilknow.yifanerp2.repository.DeliveryRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DeliveryService {

  @Resource
  private DeliveryRepository deliveryRepository;

  public Delivery create(Delivery delivery) {
    delivery.setCreateDate(new Date());
    delivery.setUpdateDate(new Date());
    return deliveryRepository.save(delivery);
  }

}

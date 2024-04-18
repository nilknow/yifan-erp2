package com.nilknow.yifanerp2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDto {

  private String serialNum;

  private Long productId;

  private Integer count;

  private Integer produceDaysTake;

  private String deliverySerialNum;

  private String customer;

  private String note;
}

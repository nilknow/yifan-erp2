package com.nilknow.yifanerp2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "\"order\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String serialNum;

  @ManyToOne
  @JoinColumn(name = "product_id",referencedColumnName = "id")
  private Product product; // Assuming a Product entity exists

  private Integer count;

  private Integer produceDaysTake;

  @ManyToOne
  @JoinColumn(name = "delivery_serial_num", referencedColumnName = "serial_num")
  private Delivery delivery;

  private String customer;

  private String note;

  private Date createTime;

  private Date updateTime;
}
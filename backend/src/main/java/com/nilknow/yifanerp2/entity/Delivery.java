package com.nilknow.yifanerp2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "delivery")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true,name = "serial_num")
  private String serialNum;

  @Enumerated(EnumType.STRING)
  private DeliveryState state = DeliveryState.PLANNING;

  private BigDecimal price=new BigDecimal(0);

  private String note;

  private Date planDate;

  @CreationTimestamp
  private Date createDate;

  @UpdateTimestamp
  private Date updateDate;

  public enum DeliveryState {
    PLANNING,
    IN_PROGRESS,
    DELIVERED,
    CANCELLED
  }

  public Delivery(String serialNum) {
    this.serialNum = serialNum;
  }
}
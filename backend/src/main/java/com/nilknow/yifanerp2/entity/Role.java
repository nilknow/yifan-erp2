package com.nilknow.yifanerp2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "company_id", nullable = false)
  private Long companyId;

  @Column(name = "description", length = 200)
  private String description;

  @Column(nullable = false, updatable = false)
  private Date createTime;

  @Column(nullable = false)
  private Date updateTime;

}

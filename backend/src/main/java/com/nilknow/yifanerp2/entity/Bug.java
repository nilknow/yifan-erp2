package com.nilknow.yifanerp2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TenantId;

import java.util.Date;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Bug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String email;
    private String phone;
    @Column(nullable = false)
    private String priority;
    @Column(nullable = false)
    private String content;
    private String feedback;
    @Column(nullable = false)
    private Date createTime;
    @TenantId
    private Long companyId;
}

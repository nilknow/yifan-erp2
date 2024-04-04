package com.nilknow.yifanerp2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TenantId;

import java.util.Date;

@Entity
@Table(name = "suggestion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String email;
    private String phone;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Date createTime;
    private String feedback;
    @TenantId
    private Long companyId;
    /**
     * 0.pending
     * 1.solved
     * 2.solving
     */
    private Integer solved;
}

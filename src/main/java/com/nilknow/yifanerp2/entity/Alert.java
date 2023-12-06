package com.nilknow.yifanerp2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    /**
     * 0:未处理 1:处理中 2:已处理
     */
    private Integer state;
    /**
     * 0:未发送 1:已发送
     */
    private Integer emailSent;
}

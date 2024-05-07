package com.nilknow.yifanerp2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "route")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String path;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Route parent;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Date updateTime;

}
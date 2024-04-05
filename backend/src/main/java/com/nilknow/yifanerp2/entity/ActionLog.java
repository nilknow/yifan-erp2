package com.nilknow.yifanerp2.entity;

import com.nilknow.yifanerp2.repository.converter.JsonbConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="action_log")
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private UUID batchId;

    @Column(nullable = false, length = 36)
    private String eventType;

    @Column(nullable = false)
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private LoginUser user;

    @Column(nullable = false, length = 36)
    private String tableName;

    @Column(nullable = false, length = 36)
    private String source;

    @Column(columnDefinition = "jsonb")
//    @Convert(converter = JsonbConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    private String additionalInfo;

    private String description;
}

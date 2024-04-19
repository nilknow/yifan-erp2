package com.nilknow.yifanerp2.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "action_log")
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
    @JdbcTypeCode(SqlTypes.JSON)
    private String additionalInfo;

    private String description;

    public ActionLog(UUID batchId,
                     String eventType,
                     Date timestamp,
                     LoginUser user,
                     String tableName,
                     String source,
                     String additionalInfo,
                     String description) {
        this.batchId = batchId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.user = user;
        this.tableName = tableName;
        this.source = source;
        this.additionalInfo = additionalInfo;
        this.description = description;
    }

    public static ActionLog add(String tableName, String source, Object obj, String desc,
                                LoginUser loginUser) throws JsonProcessingException {
        return new ActionLog(
                UUID.randomUUID(),
                "add",
                new Date(),
                loginUser,
                tableName,
                source,
                new ObjectMapper().writeValueAsString(obj),
                desc
        );
    }
}

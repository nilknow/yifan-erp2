package com.nilknow.yifanerp2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ActionLogDto {
    private Long id;
    private UUID batchId;
    private String eventType;
    private Date timestamp;
    private String username;
    private String additionalInfo;
    private String description;
}

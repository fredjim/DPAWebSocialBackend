package com.infsis.socialpagebackend.moderation.dtos;

import com.infsis.socialpagebackend.moderation.enums.BlacklistCategory;
import lombok.Data;

import java.time.Instant;

@Data
public class BlacklistWordDTO {

    private Long id;
    private String word;
    private BlacklistCategory category;
    private boolean active;
    private boolean excludeFromFile;
    private String notes;
    private Instant createdAt;
}

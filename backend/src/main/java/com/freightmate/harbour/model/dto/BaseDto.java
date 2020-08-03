package com.freightmate.harbour.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseDto {
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String deletedBy;
    private String createdBy;
    private String updatedBy;

    // todo: need to implement the base function to populate the value of the variables above
}

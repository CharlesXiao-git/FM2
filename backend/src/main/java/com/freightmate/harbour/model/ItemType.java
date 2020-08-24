package com.freightmate.harbour.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ItemType extends BaseEntity<Long> {

    @Column(nullable = false)
    private String name;

    private Long quantity;
    private Float length;
    private Float width;
    private Float height;
    private Float weight;
    private Boolean isMutable = true;
    private Boolean isCustom = false;
}

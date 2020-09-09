package com.freightmate.harbour.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ItemTemplate extends BaseEntity<Long> {

    @JsonBackReference
    @OneToOne(targetEntity = ItemType.class, fetch = FetchType.LAZY)
    private ItemType itemType;

    @Column(name = "item_type_id", insertable = false, updatable = false)
    private long itemTypeId;

    @Column(nullable = false)
    private Long quantity;
    @Column(nullable = false)
    private Float length;
    @Column(nullable = false)
    private Float width;
    @Column(nullable = false)
    private Float height;
    @Column(nullable = false)
    private Float weight;
    @Column(nullable = false)
    private Float totalWeight;
    @Column(nullable = false)
    private Float volume;
    @Column(nullable = false)
    private Boolean isHazardous;
}

package com.freightmate.harbour.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Item extends BaseEntity<Long> {

    @JsonBackReference
    @ManyToOne(targetEntity = Consignment.class,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "consignment_id")
    private Consignment consignment;

    @Column(name = "consignment_id", insertable = false, updatable = false)
    private long consignmentId;

    @Column(nullable = false)
    private Long quantity;

    private long itemTypeId;

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

    @Column(nullable = false)
    Boolean isDeleted;
    LocalDateTime deletedAt;
    String deletedBy;

    public Item() {
        this.isHazardous = false;
        this.isDeleted = false;
    }
}
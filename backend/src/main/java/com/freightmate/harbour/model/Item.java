package com.freightmate.harbour.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

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

    // todo Quote model
    // @Column(name = "quote_id", insertable = false, updatable = false)
    private Long quoteId;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(targetEntity = ItemType.class, fetch = FetchType.EAGER)
    private ItemType itemType;

    @Column(name = "item_type_id", insertable = false, updatable = false)
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

    public Item() {
        this.isHazardous = false;
        this.isDeleted = false;
    }
}
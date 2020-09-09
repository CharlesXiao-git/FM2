package com.freightmate.harbour.model;

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
public class ItemType extends BaseEntity<Long> {

    @OneToOne(targetEntity = ItemTemplate.class, mappedBy = "itemType", fetch = FetchType.EAGER)
    private ItemTemplate itemTemplate;

    @Column(nullable = false)
    private String type;

    private Boolean isMutable = true;
}

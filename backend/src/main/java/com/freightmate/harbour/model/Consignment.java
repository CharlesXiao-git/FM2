package com.freightmate.harbour.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freightmate.harbour.model.dto.ItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@SuperBuilder
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Consignment extends BaseEntity<Long> {

    @JsonIgnore
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User owner;

    @Column(name = "owner_id", insertable = false, updatable = false)
    private long ownerId;

    // TODO remove the notfound action once good data is ensured
    @ManyToOne(targetEntity = Address.class, fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Address senderAddress;

    @Column(name = "sender_address_id", insertable = false, updatable = false)
    private long senderAddressId;

    // TODO remove the notfound action once good data is ensured
    @ManyToOne(targetEntity = Address.class, fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private Address deliveryAddress;

    @Column(name = "delivery_address_id", insertable = false, updatable = false)
    private long deliveryAddressId;

    private String connoteId;

    private LocalDateTime dispatchDateAt;
    private LocalDateTime deliveryWindowStartAt;
    private LocalDateTime deliveryWindowEndAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressClass addressClass;

    @Column(nullable = false)
    private Boolean isAllowedToLeave;

    @Column(nullable = false)
    private Boolean isTailgateRequired;

    @OneToMany(targetEntity = Item.class,
            cascade = CascadeType.ALL,
            mappedBy = "consignment",
            orphanRemoval = true)
    private List<Item> items;

    @Column(nullable = false)
    Boolean isDeleted;
    LocalDateTime deletedAt;
    Long deletedBy;

    public Consignment() {
        this.isAllowedToLeave = false;
        this.isTailgateRequired = false;
        this.isDeleted = false;
    }

    public void setItems(List<Item> items) {
        if(Objects.nonNull(this.items)) {
            this.items.clear();
            this.items.addAll(items);
        } else {
            this.items = items;
        }

    }
}

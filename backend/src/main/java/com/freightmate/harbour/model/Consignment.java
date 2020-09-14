package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Consignment extends BaseEntity<Long> {

    @ManyToOne(targetEntity = UserClient.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_client_id")
    private UserClient userClient;

    @Column(name = "user_client_id", insertable = false, updatable = false)
    private long userClientId;

    // todo Quote model
    // @Column(name = "quote_id", insertable = false, updatable = false)
     private Long quoteId;

    // todo Manifest model
    // @Column(name = "manifest_id", insertable = false, updatable = false)
     private Long manifestId;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressClass deliveryAddressClass;

    private String connoteNumber;
    private String whoPays;
    private String paymentAccountNumber;
    private Boolean authorityToLeave;
    private LocalDateTime dispatchedAt;
    private LocalDateTime deliveryWindowBegin;
    private LocalDateTime deliveryWindowEnd;
    private String specialInstructions;
    private Float scaleFrom;
    private Float scaleTo;
    private Float baseCharge;
    private Float rateCharge;
    private Float minCharge;
    private Float fuel;
    private Float cubicConversionRate;
    private ConsignmentType consignmentType;
    private Float residentialAddressCharge;
    private Boolean isTailgateRequired;
    private Float tailgateCharge;
    private Float dgCharge;
    private Float oversizeCharge;
    private String status;
    private LocalDateTime statusBooked;
    private LocalDateTime statusInTransit;
    private LocalDateTime statusOutForDelivery;
    private LocalDateTime statusDelivered;
    private LocalDateTime statusUnableToDeliver;
    private String statusNote;
    private Boolean statusLate;
    private LocalDateTime statusLatestTimestamp;
    private Float totalItemWeight;
    private Float totalItemCubic;
    private Integer totalItemQty;
    private Float freightCost;

    @Column(name = "cat_1_fee")
    private Float cat1Fee;
    private Float fuelLevy;

    @Column(name = "cat_2_fee")
    private Float cat2Fee;
    private Float gst;
    private Float totalCost;
    private Boolean poa;

    @Column(name = "service_title_1")
    private String serviceTitle1;
    @Column(name = "service_title_2")
    private String serviceTitle2;
    @Column(name = "service_title_3")
    private String serviceTitle3;

    @OneToMany(targetEntity = Item.class,
            cascade = CascadeType.ALL,
            mappedBy = "consignment",
            orphanRemoval = true)
    private List<Item> items;

    @OneToMany(targetEntity = Offer.class,
            mappedBy = "consignment",
            fetch = FetchType.LAZY
    )
    private List<Offer> offers;

    @OneToMany(
            targetEntity = Offer.class,
            mappedBy = "consignment",
            fetch = FetchType.EAGER
    )
    @Where(clause = "selected = true")
    private List<Offer> selectedOffer;

    public Offer getSelectedOffer () {
        if (Objects.isNull(this.selectedOffer) || this.selectedOffer.isEmpty()) {
            return null;
        }
        return this.selectedOffer.get(0);
    }

    public Consignment() {
        this.authorityToLeave = false;
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

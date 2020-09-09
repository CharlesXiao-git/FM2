package com.freightmate.harbour.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@Entity
public class Offer extends BaseEntity<Long> {

    @ManyToOne(targetEntity = CarrierAccount.class,
            fetch = FetchType.EAGER
    )
    private CarrierAccount carrierAccount;

    @Column(name = "carrier_account_id", insertable = false, updatable = false)
    private long carrierAccountId;

    @JsonBackReference
    @ManyToOne(targetEntity = Consignment.class,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "consignment_id")
    private Consignment consignment;

    private Integer ETA;
    private Float freightCost;
    @Column(name = "category_1_fees") private Float category1Fees;
    @Column(name = "category_2_fees") private Float category2Fees;
    private Float fuelSurcharge;
    private Float gst;
    private Float totalCost;
    private boolean selected;

    public Offer(){
        selected = false;
    }

    public Offer(
            CarrierAccount carrierAccount,
            Consignment consignment,
            Integer ETA,
            Float freightCost,
            Float category1Fees,
            Float category2Fees
    ){
        this.carrierAccount = carrierAccount;
        this.consignment = consignment;
        this.selected = false;

        this.ETA = ETA;
        this.freightCost = freightCost;
        this.category1Fees = category1Fees;
        this.category2Fees = category2Fees;

        this.fuelSurcharge = freightCost * (carrierAccount.getFuelCharge() / 100);
        this.totalCost = (freightCost + category1Fees + category2Fees + this.fuelSurcharge);
        this.gst = this.totalCost * 1.1f;
    }
}

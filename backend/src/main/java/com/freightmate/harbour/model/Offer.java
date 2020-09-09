package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class Offer extends BaseEntity<Long> {

    @ManyToOne
    CarrierAccount carrierAccount;
    Integer ETA;
    Float freightCost;
    Float category1Fees;
    Float category2Fees;
    Float fuelSurcharge;
    Float gst;
    Float totalCost;
    @ManyToOne
    Consignment consignment;
    boolean selected;

    Offer(){
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

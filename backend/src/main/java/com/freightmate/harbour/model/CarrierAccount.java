package com.freightmate.harbour.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CarrierAccount extends BaseEntity<Long> {
    private String accountNumber;

    @ManyToOne(targetEntity = Carrier.class, fetch = FetchType.LAZY)
    private Carrier carrier;

    @Column(name = "carrier_id", insertable = false, updatable = false)
    private Long carrierId;

    @ManyToOne(targetEntity = UserBroker.class, fetch = FetchType.LAZY)
    private UserBroker userBroker;

    @Column(name = "user_broker_id", insertable = false, updatable = false)
    private Long userBrokerId;

    @ManyToOne(targetEntity = UserCustomer.class, fetch = FetchType.LAZY)
    private UserCustomer userCustomer;

    @Column(name = "user_customer_id", insertable = false, updatable = false)
    private Long userCustomerId;

    @Column(nullable = false)
    private Boolean isDefault;

    private Float ruleResAddressCost;
    private Float ruleTailgateCost;
    private Float ruleDgCost;
    @Column(name = "rule_oversize_cat1_cost") private Float ruleOversizeCat1Cost;
    @Column(name = "rule_oversize_cat2_cost") private Float ruleOversizeCat2Cost;
    @Column(name = "rule_oversize_cat3_cost") private Float ruleOversizeCat3Cost;
    @Column(name = "rule_oversize_cat4_cost") private Float ruleOversizeCat4Cost;
    @Column(name = "rule_oversize_cat5_cost") private Float ruleOversizeCat5Cost;
    @Column(name = "rule_oversize_cat1_size") private Float ruleOversizeCat1Size;
    @Column(name = "rule_oversize_cat2_size") private Float ruleOversizeCat2Size;
    @Column(name = "rule_oversize_cat3_size") private Float ruleOversizeCat3Size;
    @Column(name = "rule_oversize_cat4_size") private Float ruleOversizeCat4Size;
    @Column(name = "rule_oversize_cat5_size") private Float ruleOversizeCat5Size;
    @Column(name = "rule_manual_handling_max_l") private String ruleManualHandlingMaxL;
    @Column(name = "rule_manual_handling_max_w") private String ruleManualHandlingMaxW;
    @Column(name = "rule_manual_handling_max_h") private String ruleManualHandlingMaxH;
    private String ruleManualHandlingMinKg;
    private String ruleManualHandlingMaxKg;
    private String ruleManualHandlingChargePer;
    private String ruleManualHandlingCharge;
    @Column(name ="fuel") private Float fuelCharge;
    private Float perKgCubic;
    private Float cartonCubic;
    @Column(name = "carton_max_h") private Float cartonMaxH;
    @Column(name = "carton_max_l") private Float cartonMaxL;
    @Column(name = "carton_max_w") private Float cartonMaxW;
    private Float cartonMaxKg;
    private Float palletCubic;
    @Column(name = "pallet_max_h") private Float palletMaxH;
    @Column(name = "pallet_max_l") private Float palletMaxL;
    @Column(name = "pallet_max_w") private Float palletMaxW;
    private Float palletMaxKg;
    private Float cartonAverageWeight;
    private String palletOvercharge;
    @Column(name = "half_pallet_max_h") private Float halfPalletMaxH;
    @Column(name = "half_pallet_max_l") private Float halfPalletMaxL;
    @Column(name = "half_pallet_max_w") private Float halfPalletMaxW;
    @Column(name = "half_pallet_max_kg") private Float halfPalletMaxKg;
    @Column(name = "rule_cat_format_1") private String ruleCatFormat1;
    @Column(name = "rule_cat_format_2") private String ruleCatFormat2;
    @Column(name = "rule_cat_format_3") private String ruleCatFormat3;
    @Column(name = "rule_cat_format_4") private String ruleCatFormat4;
    @Column(name = "rule_cat_format_5") private String ruleCatFormat5;

    public CarrierAccount() {
        this.isDefault = false;
        this.isDeleted = false;
    }
}

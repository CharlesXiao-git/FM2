package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarrierZone {
    @ManyToOne(targetEntity = Suburb.class, fetch = FetchType.LAZY)
    private Suburb suburb;

    @Column(name = "suburb_id", insertable = false, updatable = false)
    private long suburbId;

    @ManyToOne(targetEntity = CarrierAccount.class, fetch = FetchType.LAZY)
    private CarrierAccount carrierAccount;

    @Column(name = "carrier_account_id", insertable = false, updatable = false)
    private long carrierAccountId;

    private String zone;

    /**
     * Convert Raw Carrier zone to string formatted like '(suburbId,carrierAccountId,zoneCode,userId)' for db insert
     * @return
     */
    public String toSqlValueString(long userId) {
        StringBuilder builder = new StringBuilder();
        return builder
                .append("(")
                .append(this.suburbId)
                .append(",")
                .append(this.carrierAccountId)
                .append(",'")
                .append(this.zone)
                .append("',")
                .append(userId)
                .append(")")
                .toString();
    }
}

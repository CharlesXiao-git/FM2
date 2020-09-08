package com.freightmate.harbour.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Carrier extends BaseEntity<Long>{
    private String name;
    private String displayName;

    @Column(nullable = false)
    private Boolean isDataTransferRequired;

    @Enumerated(EnumType.STRING)
    private DataTransferFrequency dataTransferFrequency;

    @Enumerated(EnumType.STRING)
    private LabelType labelType;

    @Column(nullable = false)
    private Boolean hasSuburbSurcharge;

    @Column(nullable = false)
    private Boolean hasSenderId;

    @JsonDeserialize(using = JsonListGenericDeserializer.class)
    @Type(type = "json")
    private List<String> serviceTypes;

    @JsonDeserialize(using = JsonListGenericDeserializer.class)
    @Type(type = "json")
    private List<String> pricing;

    @JsonDeserialize(using = JsonListGenericDeserializer.class)
    @Type(type = "json")
    private List<String> metroZones;

    @Column(nullable = false)
    private Boolean isInternalInvoicing;

    @Column(nullable = false)
    private Boolean isTrackingDifot;

    private String trackingUrl;

    public Carrier() {
        this.isDataTransferRequired = false;
        this.hasSuburbSurcharge = false;
        this.hasSenderId = false;
        this.isInternalInvoicing = false;
        this.isTrackingDifot = false;
        this.isDeleted = false;
    }
}

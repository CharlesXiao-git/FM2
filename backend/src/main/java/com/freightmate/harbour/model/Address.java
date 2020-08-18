package com.freightmate.harbour.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Address extends BaseEntity<Long> {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    private Long customerId;
    private Long clientId;

    private String referenceId;

    @Column(nullable = false)
    private String companyName;

    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(nullable = false)
    private String town;

    @Column(nullable = false)
    //@Size(max = 4, min = 3)       // Commented, existing data contains LENGTH(postcode) > 4
    private Integer postcode;

    @Column(nullable = false)
    private static final String country = "Australia";

    @Column(nullable = false)
    private String state;           // State is not using enum due to existing data

    @Column(nullable = false)
    private String contactName;

    private String contactNo;

    private String contactEmail;

    private String notes; // Special Instructions

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private Boolean isDefault = false;

    private Integer countUsed;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    Boolean isDeleted = false;
    LocalDateTime deletedAt;
    Long deletedBy;
}

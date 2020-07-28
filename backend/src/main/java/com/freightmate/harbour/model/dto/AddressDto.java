package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.Address;
import com.freightmate.harbour.model.AddressType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto extends BaseDto{
    private long id;
    private AddressType addressType;
    private Long customerId;
    private Long clientId;
    private String referenceId;
    private String companyName;
    private String addressLine1;
    private String addressLine2;
    private String town;
    private Integer postcode;
    private String country = "Australia";
    private String state;
    private String contactName;
    private String contactNo;
    private String contactEmail;
    private String notes; // Special Instructions
    private Boolean isDefault = false;
    private int countUsed;
    private Boolean isDeleted = false;

    public Address convertDtoToAddress() {
        return Address.builder()
                .id(this.id)
                .addressLine1(this.addressLine1)
                .addressLine2(this.addressLine2)
                .addressType(this.addressType)
                .clientId(this.clientId)
                .customerId(this.customerId)
                .companyName(this.companyName)
                .contactEmail(this.contactEmail)
                .contactName(this.contactName)
                .contactNo(this.contactNo)
                .country(this.country)
                .countUsed(this.countUsed)
                .createdAt(super.getCreatedAt())
                .createdBy(super.getCreatedBy())
                .deletedAt(super.getDeletedAt())
                .deletedBy(super.getDeletedBy())
                .isDefault(this.isDefault)
                .isDeleted(this.isDeleted)
                .notes(this.notes)
                .postcode(this.postcode)
                .referenceId(this.referenceId)
                .state(this.state)
                .town(this.town)
                .updatedAt(super.getUpdatedAt())
                .updatedBy(super.getUpdatedBy())
                .build();
    }
}

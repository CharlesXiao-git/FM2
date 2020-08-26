package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.Address;
import com.freightmate.harbour.model.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {
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
    private String state;
    private String contactName;
    private String contactNo;
    private String contactEmail;
    private String notes; // Special Instructions
    private Boolean isDefault = false;
    private Integer countUsed;
    private Boolean isDeleted = false;

    public static Address toAddress(AddressDTO dto) {
        return Address.builder()
                .id(dto.id)
                .addressLine1(dto.addressLine1)
                .addressLine2(dto.addressLine2)
                .addressType(dto.addressType)
                .clientId(dto.clientId)
                .customerId(dto.customerId)
                .companyName(dto.companyName)
                .contactEmail(dto.contactEmail)
                .contactName(dto.contactName)
                .contactNo(dto.contactNo)
                .countUsed(dto.countUsed)
                .isDefault(dto.isDefault)
                .isDeleted(dto.isDeleted)
                .notes(dto.notes)
                .postcode(dto.postcode)
                .referenceId(dto.referenceId)
                .state(dto.state)
                .town(dto.town)
                .build();
    }

    public static AddressDTO fromAddress(Address address) {

        if(Objects.isNull(address)) {
            return null;
        }

        return AddressDTO.builder()
                .id(address.getId())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .addressType(address.getAddressType())
                .clientId(address.getClientId())
                .customerId(address.getCustomerId())
                .companyName(address.getCompanyName())
                .contactEmail(address.getContactEmail())
                .contactName(address.getContactName())
                .contactNo(address.getContactNo())
                .countUsed(address.getCountUsed())
                .isDefault(address.getIsDefault())
                .notes(address.getNotes())
                .postcode(address.getPostcode())
                .referenceId(address.getReferenceId())
                .state(address.getState())
                .town(address.getTown())
                .build();
    }
}

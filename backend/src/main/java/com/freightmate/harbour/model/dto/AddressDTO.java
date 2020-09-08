package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.Address;
import com.freightmate.harbour.model.AddressType;
import com.freightmate.harbour.model.User;
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
    private Long userClientId;
    private String referenceId;
    private String company;
    private String addressLine1;
    private String addressLine2;
    private SuburbDTO suburb;
    private String contactName;
    private String phoneNumber;
    private String email;
    private String specialInstructions;
    private Boolean isDefault = false;
    private Boolean isDeleted = false;

    public static Address toAddress(AddressDTO dto, User owner) {
        Address.AddressBuilder<?, ?> builder = Address.builder();

        if (Objects.nonNull(owner)) {
            if(owner.isUserBroker()) {
                builder.userBroker(owner.getUserBroker());
            }
            if(owner.isUserCustomer()) {
                builder.userCustomer(owner.getUserCustomer());
                builder.userBroker(owner.getUserCustomer().getUserBroker());
            }
            if(owner.isUserClient()) {
                builder.userBroker(owner.getUserClient().getUserCustomer().getUserBroker());
                builder.userCustomer(owner.getUserClient().getUserCustomer());
                builder.userClient(owner.getUserClient());
            }
        }
        return builder
                .id(dto.id)
                .addressLine1(dto.addressLine1)
                .addressLine2(dto.addressLine2)
                .addressType(dto.addressType)
                .company(dto.company)
                .email(dto.email)
                .contactName(dto.contactName)
                .phoneNumber(dto.phoneNumber)
                .isDefault(dto.isDefault)
                .isDeleted(dto.isDeleted)
                .specialInstructions(dto.specialInstructions)
                .suburb(SuburbDTO.toSuburb(dto.suburb))
                .referenceId(dto.referenceId)
                .build();
    }

    public static AddressDTO fromAddress(Address address) {
        // todo: create helper function that receive an entity to get the owner ID
        if(Objects.isNull(address)) {
            return null;
        }

        AddressDTOBuilder builder = AddressDTO.builder();

        if(Objects.nonNull(address.getUserClientId())) {
            builder.userClientId(address.getUserClient().getUserId());
        } else if (Objects.nonNull(address.getUserCustomerId())) {
            builder.userClientId(address.getUserCustomer().getUserId());
        }else {
            builder.userClientId(address.getUserBroker().getUserId());
        }

        return builder
                .id(address.getId())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .addressType(address.getAddressType())
                .company(address.getCompany())
                .email(address.getEmail())
                .contactName(address.getContactName())
                .phoneNumber(address.getPhoneNumber())
                .isDefault(address.getIsDefault())
                .specialInstructions(address.getSpecialInstructions())
                .suburb(SuburbDTO.fromSuburb(address.getSuburb()))
                .referenceId(address.getReferenceId())
                .build();
    }
}

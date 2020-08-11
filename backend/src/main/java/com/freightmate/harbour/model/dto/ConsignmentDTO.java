package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.AddressClass;
import com.freightmate.harbour.model.Consignment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ConsignmentDTO {
    private long id;
    private long clientId;
    private long senderAddressId;
    private long deliveryAddressId;
    private String connoteId;
    private LocalDateTime dispatchDateAt;
    private LocalDateTime deliveryWindowStartAt;
    private LocalDateTime deliveryWindowEndAt;
    private AddressClass addressClass;
    private Boolean isAllowedToLeave;
    private Boolean isTailgateRequired;

    public static Consignment toConsignment(ConsignmentDTO dto) {
        return Consignment.builder()
                .id(dto.id)
                .clientId(dto.clientId)
                .senderAddressId(dto.senderAddressId)
                .deliveryAddressId(dto.deliveryAddressId)
                .connoteId(dto.connoteId)
                .dispatchDateAt(dto.dispatchDateAt)
                .deliveryWindowStartAt(dto.deliveryWindowStartAt)
                .deliveryWindowEndAt(dto.deliveryWindowEndAt)
                .addressClass(dto.addressClass)
                .isAllowedToLeave(dto.isAllowedToLeave)
                .isTailgateRequired(dto.isTailgateRequired)
                .build();
    }

    public static ConsignmentDTO fromConsignment(Consignment address) {
        return ConsignmentDTO.builder()
                .id(address.getId())
                .clientId(address.getClientId())
                .senderAddressId(address.getSenderAddressId())
                .deliveryAddressId(address.getDeliveryAddressId())
                .connoteId(address.getConnoteId())
                .dispatchDateAt(address.getDispatchDateAt())
                .deliveryWindowStartAt(address.getDeliveryWindowStartAt())
                .deliveryWindowEndAt(address.getDeliveryWindowEndAt())
                .addressClass(address.getAddressClass())
                .isAllowedToLeave(address.getIsAllowedToLeave())
                .isTailgateRequired(address.getIsTailgateRequired())
                .build();
    }

}



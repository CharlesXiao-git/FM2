package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.AddressClass;
import com.freightmate.harbour.model.Consignment;
import com.freightmate.harbour.model.ConsignmentType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ConsignmentDTO {
    private long id;
    private long ownerId;
    private long senderAddressId;
    private long deliveryAddressId;
    private String connoteNumber;
    private AddressDTO senderAddress;
    private AddressDTO deliveryAddress;
    private LocalDateTime dispatchedAt;
    private LocalDateTime deliveryWindowBegin;
    private LocalDateTime deliveryWindowEnd;
    private AddressClass deliveryAddressClass;
    private Boolean authorityToLeave;
    private Boolean isTailgateRequired;
    private ConsignmentType consignmentType;
    private List<ItemDTO> items;
    private OfferDTO selectedOffer;

    public static Consignment toConsignment(ConsignmentDTO dto) {
        return Consignment.builder()
            .id(dto.id)
            .userClientId(dto.ownerId)
            .senderAddressId(dto.senderAddressId)
            .deliveryAddressId(dto.deliveryAddressId)
            .connoteNumber(dto.connoteNumber)
            .dispatchedAt(dto.dispatchedAt)
            .deliveryWindowBegin(dto.deliveryWindowBegin)
            .deliveryWindowEnd(dto.deliveryWindowEnd)
            .deliveryAddressClass(dto.deliveryAddressClass)
            .authorityToLeave(dto.authorityToLeave)
            .isTailgateRequired(dto.isTailgateRequired)
            .consignmentType(dto.consignmentType)
            .items(dto.items.stream()
                    .map(ItemDTO::toItem)
                    .collect(Collectors.toList())
            )
            .selectedOffer(Collections.singletonList(OfferDTO.toOffer(dto.selectedOffer)))
            .build();
    }

    public static ConsignmentDTO fromConsignment(Consignment con) {

        if (Objects.isNull(con)) {
            return null;
        }

        ConsignmentDTOBuilder builder = ConsignmentDTO.builder();

        // Set the selectedOffer to the ConsignmentDTO if exists
        if (Objects.nonNull(con.getSelectedOffer())) {
            builder.selectedOffer(OfferDTO.fromOffer(con.getSelectedOffer()));
        }

        return builder
            .id(con.getId())
            .ownerId(con.getUserClient().getUserId())
            .senderAddressId(con.getSenderAddressId())
            .deliveryAddressId(con.getDeliveryAddressId())
            .connoteNumber(con.getConnoteNumber())
            .senderAddress(AddressDTO.fromAddress(con.getSenderAddress()))
            .deliveryAddress(AddressDTO.fromAddress(con.getDeliveryAddress()))
            .dispatchedAt(con.getDispatchedAt())
            .deliveryWindowBegin(con.getDeliveryWindowBegin())
            .deliveryWindowEnd(con.getDeliveryWindowEnd())
            .deliveryAddressClass(con.getDeliveryAddressClass())
            .authorityToLeave(con.getAuthorityToLeave())
            .isTailgateRequired(con.getIsTailgateRequired())
            .consignmentType(con.getConsignmentType())
            .items(con.getItems().stream()
                    .map(ItemDTO::fromItem)
                    .collect(Collectors.toList())
            )
            .build();
    }
}


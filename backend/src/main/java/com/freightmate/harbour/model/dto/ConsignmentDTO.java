package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.AddressClass;
import com.freightmate.harbour.model.Consignment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

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
    private AddressDTO senderAddress;
    private AddressDTO deliveryAddress;
    private String connoteId;
    private LocalDateTime dispatchDateAt;
    private LocalDateTime deliveryWindowStartAt;
    private LocalDateTime deliveryWindowEndAt;
    private AddressClass addressClass;
    private Boolean isAllowedToLeave;
    private Boolean isTailgateRequired;
    private List<ItemDTO> items;

    public static Consignment toConsignment(ConsignmentDTO dto) {
        return Consignment.builder()
                .id(dto.id)
                .ownerId(dto.ownerId)
                .senderAddressId(dto.senderAddressId)
                .deliveryAddressId(dto.deliveryAddressId)
                .connoteId(dto.connoteId)
                .dispatchDateAt(dto.dispatchDateAt)
                .deliveryWindowStartAt(dto.deliveryWindowStartAt)
                .deliveryWindowEndAt(dto.deliveryWindowEndAt)
                .addressClass(dto.addressClass)
                .isAllowedToLeave(dto.isAllowedToLeave)
                .isTailgateRequired(dto.isTailgateRequired)
                .items(dto.items.stream()
                        .map(ItemDTO::toItem)
                        .collect(Collectors.toList())
                )
                .build();
    }

    public static ConsignmentDTO fromConsignment(Consignment con) {

        if (Objects.isNull(con)) {
            return null;
        }

        return ConsignmentDTO.builder()
                .id(con.getId())
                .ownerId(con.getOwnerId())
                .senderAddressId(con.getSenderAddressId())
                .deliveryAddressId(con.getDeliveryAddressId())
                .senderAddress(AddressDTO.fromAddress(con.getSenderAddress()))
                .deliveryAddress(AddressDTO.fromAddress(con.getDeliveryAddress()))
                .connoteId(con.getConnoteId())
                .dispatchDateAt(con.getDispatchDateAt())
                .deliveryWindowStartAt(con.getDeliveryWindowStartAt())
                .deliveryWindowEndAt(con.getDeliveryWindowEndAt())
                .addressClass(con.getAddressClass())
                .isAllowedToLeave(con.getIsAllowedToLeave())
                .isTailgateRequired(con.getIsTailgateRequired())
                .items(con.getItems().stream()
                        .map(ItemDTO::fromItem)
                        .collect(Collectors.toList())
                )
                .build();
    }

}



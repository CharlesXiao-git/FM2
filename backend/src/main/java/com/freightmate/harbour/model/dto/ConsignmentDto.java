package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.DeliveryAddressType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ConsignmentDto {
    private long id;
    private long clientId;
    private long senderAddressId;
    private long deliveryAddressId;
    private String connoteId;
    private LocalDateTime dispatchDateAt;
    private LocalDateTime deliveryWindowStartAt;
    private LocalDateTime deliveryWindowEndAt;
    private DeliveryAddressType deliveryAddressType;
    private boolean isAllowedToLeave;
    private boolean isTailgateRequired;
}

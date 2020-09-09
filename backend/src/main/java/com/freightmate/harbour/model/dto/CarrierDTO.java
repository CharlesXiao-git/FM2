package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.Carrier;
import com.freightmate.harbour.model.DataTransferFrequency;
import com.freightmate.harbour.model.LabelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarrierDTO {
    private long id;
    private String name;
    private String displayName;
    private Boolean isDataTransferRequired;
    private DataTransferFrequency dataTransferFrequency;
    private LabelType labelType;
    private Boolean hasSuburbSurcharge;
    private Boolean hasSenderId;
    private List<String> serviceTypes;
    private List<String> pricing;
    private List<String> metroZones;
    private Boolean isInternalInvoicing;
    private Boolean isTrackingDifot;
    private String trackingUrl;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;

    public static Carrier toCarrier(CarrierDTO dto) {
        return Carrier.builder()
            .id(dto.id)
            .name(dto.name)
            .displayName(dto.displayName)
            .isDataTransferRequired(dto.isDataTransferRequired)
            .dataTransferFrequency(dto.dataTransferFrequency)
            .labelType(dto.labelType)
            .hasSuburbSurcharge(dto.hasSuburbSurcharge)
            .hasSenderId(dto.hasSenderId)
            .serviceTypes(dto.serviceTypes)
            .pricing(dto.pricing)
            .metroZones(dto.metroZones)
            .isInternalInvoicing(dto.isInternalInvoicing)
            .isTrackingDifot(dto.isTrackingDifot)
            .trackingUrl(dto.trackingUrl)
            .isDeleted(dto.isDeleted)
            .deletedAt(dto.deletedAt)
                .build();
    }

    public static CarrierDTO fromCarrier(Carrier car) {
        return CarrierDTO.builder()
                .id(car.getId())
                .name(car.getName())
                .displayName(car.getDisplayName())
                .isDataTransferRequired(car.getIsDataTransferRequired())
                .dataTransferFrequency(car.getDataTransferFrequency())
                .labelType(car.getLabelType())
                .hasSuburbSurcharge(car.getHasSuburbSurcharge())
                .hasSenderId(car.getHasSenderId())
                .serviceTypes(car.getServiceTypes())
                .pricing(car.getPricing())
                .metroZones(car.getMetroZones())
                .isInternalInvoicing(car.getIsInternalInvoicing())
                .isTrackingDifot(car.getIsTrackingDifot())
                .trackingUrl(car.getTrackingUrl())
                .isDeleted(car.getIsDeleted())
                .deletedAt(car.getDeletedAt())
                .build();
    }
}

package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.Manifest;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ManifestDTO {
    private long id;
    private long userClientId;
    private Boolean goodsPickupStatus;
    private String manifestNumber;
    private String readyTime;
    private String closingTime;
    private String dataStatus;
    private List<ConsignmentDTO> consignments;
    private LocalDateTime createdAt;


    public static Manifest toManifest(ManifestDTO dto) {
        return Manifest.builder()
                .id(dto.id)
                .userClientId(dto.userClientId)
                .goodsPickupStatus(dto.goodsPickupStatus)
                .manifestNumber(dto.manifestNumber)
                .readyTime(dto.readyTime)
                .closingTime(dto.closingTime)
                .dataStatus(dto.dataStatus)
                .consignments(
                        dto.consignments.stream()
                                .map(ConsignmentDTO::toConsignment)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public static ManifestDTO fromManifest(Manifest manifest) {

        if (Objects.isNull(manifest)) {
            return null;
        }

        return ManifestDTO.builder()
                .id(manifest.getId())
                .userClientId(manifest.getUserClient().getUserId())
                .goodsPickupStatus(manifest.getGoodsPickupStatus())
                .manifestNumber(manifest.getManifestNumber())
                .readyTime(manifest.getReadyTime())
                .closingTime(manifest.getClosingTime())
                .dataStatus(manifest.getDataStatus())
                .consignments(
                        manifest.getConsignments().stream()
                                .map(ConsignmentDTO::fromConsignment)
                                .collect(Collectors.toList())
                )
                .createdAt(manifest.getCreatedAt())
                .build();
    }
}

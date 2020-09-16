package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.Offer;
import lombok.*;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OfferDTO {
  private long id;
  private CarrierAccountDTO carrierAccount;
  private boolean selected;
  private Integer ETA;
  private Float freightCost;
  private Float category1Fees;
  private Float category2Fees;
  private Float fuelSurcharge;
  private Float gst;
  private Float totalCost;

  public static Offer toOffer(OfferDTO dto) {
    return Offer.builder()
        .id(dto.id)
        .carrierAccount(CarrierAccountDTO.toCarrierAccount(dto.carrierAccount))
        .selected(dto.selected)
        .ETA(dto.ETA)
        .freightCost(dto.freightCost)
        .category1Fees(dto.category1Fees)
        .category2Fees(dto.category2Fees)
        .fuelSurcharge(dto.fuelSurcharge)
        .gst(dto.gst)
        .totalCost(dto.totalCost)
        .build();
  }

  public static OfferDTO fromOffer(Offer offer) {
    if (Objects.isNull(offer)) {
      return null;
    }

    return OfferDTO.builder()
        .id(offer.getId())
        .carrierAccount(CarrierAccountDTO.fromCarrierAccount(offer.getCarrierAccount()))
        .selected(offer.isSelected())
        .ETA(offer.getETA())
        .freightCost(offer.getFreightCost())
        .category1Fees(offer.getCategory1Fees())
        .category2Fees(offer.getCategory2Fees())
        .fuelSurcharge(offer.getFuelSurcharge())
        .gst(offer.getGst())
        .totalCost(offer.getTotalCost())
        .build();
  }
}

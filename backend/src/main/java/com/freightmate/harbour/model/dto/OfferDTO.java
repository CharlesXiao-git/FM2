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

  public static Offer toOffer(OfferDTO dto) {
    return Offer.builder()
        .id(dto.id)
        .carrierAccount(CarrierAccountDTO.toCarrierAccount(dto.carrierAccount))
        .selected(dto.selected)
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
        .build();
  }

}

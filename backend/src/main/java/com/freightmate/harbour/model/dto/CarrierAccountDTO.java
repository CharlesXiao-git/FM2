package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.CarrierAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarrierAccountDTO {
  private long id;
  private CarrierDTO carrier;

  public static CarrierAccount toCarrierAccount(CarrierAccountDTO dto) {
    return CarrierAccount.builder()
        .id(dto.id)
        .carrier(CarrierDTO.toCarrier(dto.carrier))
        .build();
  }

  public static CarrierAccountDTO fromCarrierAccount(CarrierAccount carrierAccount) {

    if (Objects.isNull(carrierAccount)) {
      return null;
    }

    return CarrierAccountDTO.builder()
        .id(carrierAccount.getId())
        .carrier(CarrierDTO.fromCarrier(carrierAccount.getCarrier()))
        .build();
  }
}

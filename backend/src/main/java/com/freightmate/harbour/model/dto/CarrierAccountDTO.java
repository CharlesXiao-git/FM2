package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.CarrierAccount;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CarrierAccountDTO {
    private long id;
    private String accountNumber;
    private Long carrierId;
    private CarrierDTO carrier;
    private Long userBrokerId;
    private Long userCustomerId;
    private Boolean isDefault;
    private Float ruleResAddressCost;
    private Float ruleTailgateCost;
    private Float ruleDgCost;
    private Float ruleOversizeCat1Cost;
    private Float ruleOversizeCat2Cost;
    private Float ruleOversizeCat3Cost;
    private Float ruleOversizeCat4Cost;
    private Float ruleOversizeCat5Cost;
    private Float ruleOversizeCat1Size;
    private Float ruleOversizeCat2Size;
    private Float ruleOversizeCat3Size;
    private Float ruleOversizeCat4Size;
    private Float ruleOversizeCat5Size;
    private String ruleManualHandlingMaxL;
    private String ruleManualHandlingMaxW;
    private String ruleManualHandlingMaxH;
    private String ruleManualHandlingMinKg;
    private String ruleManualHandlingMaxKg;
    private String ruleManualHandlingChargePer;
    private String ruleManualHandlingCharge;
    private Float fuelCharge;
    private Float perKgCubic;
    private Float cartonCubic;
    private Float cartonMaxH;
    private Float cartonMaxL;
    private Float cartonMaxW;
    private Float cartonMaxKg;
    private Float palletCubic;
    private Float palletMaxH;
    private Float palletMaxL;
    private Float palletMaxW;
    private Float palletMaxKg;
    private Float cartonAverageWeight;
    private String palletOvercharge;
    private Float halfPalletMaxH;
    private Float halfPalletMaxL;
    private Float halfPalletMaxW;
    private Float halfPalletMaxKg;
    private String ruleCatFormat1;
    private String ruleCatFormat2;
    private String ruleCatFormat3;
    private String ruleCatFormat4;
    private String ruleCatFormat5;

    public static CarrierAccount toCarrierAccount(CarrierAccountDTO dto) {
        CarrierAccount.CarrierAccountBuilder<?, ?> builder = CarrierAccount.builder();

        if(Objects.isNull(dto.isDefault)) {
            builder.isDefault(false);
        } else {
            builder.isDefault(dto.isDefault);
        }


        return builder
                .id(dto.id)
                .accountNumber(dto.accountNumber)
                .carrierId(dto.carrierId)
                .carrier(Objects.nonNull(dto.carrier) ? CarrierDTO.toCarrier(dto.carrier) : null)
                .userBrokerId(dto.userBrokerId)
                .userCustomerId(dto.userCustomerId)
                .ruleResAddressCost(dto.ruleResAddressCost)
                .ruleTailgateCost(dto.ruleTailgateCost)
                .ruleDgCost(dto.ruleDgCost)
                .ruleOversizeCat1Cost(dto.ruleOversizeCat1Cost)
                .ruleOversizeCat2Cost(dto.ruleOversizeCat2Cost)
                .ruleOversizeCat3Cost(dto.ruleOversizeCat3Cost)
                .ruleOversizeCat4Cost(dto.ruleOversizeCat4Cost)
                .ruleOversizeCat5Cost(dto.ruleOversizeCat5Cost)
                .ruleOversizeCat1Size(dto.ruleOversizeCat1Size)
                .ruleOversizeCat2Size(dto.ruleOversizeCat2Size)
                .ruleOversizeCat3Size(dto.ruleOversizeCat3Size)
                .ruleOversizeCat4Size(dto.ruleOversizeCat4Size)
                .ruleOversizeCat5Size(dto.ruleOversizeCat5Size)
                .fuelCharge(dto.fuelCharge)
                .perKgCubic(dto.perKgCubic)
                .cartonCubic(dto.cartonCubic)
                .cartonMaxH(dto.cartonMaxH)
                .cartonMaxL(dto.cartonMaxL)
                .cartonMaxW(dto.cartonMaxW)
                .cartonMaxKg(dto.cartonMaxKg)
                .palletCubic(dto.palletCubic)
                .palletMaxH(dto.palletMaxH)
                .palletMaxL(dto.palletMaxL)
                .palletMaxW(dto.palletMaxW)
                .palletMaxKg(dto.palletMaxKg)
                .cartonAverageWeight(dto.cartonAverageWeight)
                .palletOvercharge(dto.palletOvercharge)
                .halfPalletMaxH(dto.halfPalletMaxH)
                .halfPalletMaxL(dto.halfPalletMaxL)
                .halfPalletMaxW(dto.halfPalletMaxW)
                .halfPalletMaxKg(dto.halfPalletMaxKg)
                .ruleCatFormat1(dto.ruleCatFormat1)
                .ruleCatFormat2(dto.ruleCatFormat2)
                .ruleCatFormat3(dto.ruleCatFormat3)
                .ruleCatFormat4(dto.ruleCatFormat4)
                .ruleCatFormat5(dto.ruleCatFormat5)
                .build();
    }

    public static CarrierAccountDTO fromCarrierAccount(CarrierAccount carrierAccount) {

        if (Objects.isNull(carrierAccount)) {
            return null;
        }

        CarrierAccountDTOBuilder builder = CarrierAccountDTO.builder();

        if (Objects.nonNull(carrierAccount.getUserCustomer())) {
            builder.userCustomerId(carrierAccount.getUserCustomer().getUserId());
        }

        return builder
                .id(carrierAccount.getId())
                .accountNumber(carrierAccount.getAccountNumber())
                .carrierId(carrierAccount.getCarrierId())
                .carrier(CarrierDTO.fromCarrier(carrierAccount.getCarrier()))
                .userBrokerId(carrierAccount.getUserBroker().getUserId())
                .isDefault(carrierAccount.getIsDefault())
                .ruleResAddressCost(carrierAccount.getRuleResAddressCost())
                .ruleTailgateCost(carrierAccount.getRuleTailgateCost())
                .ruleDgCost(carrierAccount.getRuleDgCost())
                .ruleOversizeCat1Cost(carrierAccount.getRuleOversizeCat1Cost())
                .ruleOversizeCat2Cost(carrierAccount.getRuleOversizeCat2Cost())
                .ruleOversizeCat3Cost(carrierAccount.getRuleOversizeCat3Cost())
                .ruleOversizeCat4Cost(carrierAccount.getRuleOversizeCat4Cost())
                .ruleOversizeCat5Cost(carrierAccount.getRuleOversizeCat5Cost())
                .ruleOversizeCat1Size(carrierAccount.getRuleOversizeCat1Size())
                .ruleOversizeCat2Size(carrierAccount.getRuleOversizeCat2Size())
                .ruleOversizeCat3Size(carrierAccount.getRuleOversizeCat3Size())
                .ruleOversizeCat4Size(carrierAccount.getRuleOversizeCat4Size())
                .ruleOversizeCat5Size(carrierAccount.getRuleOversizeCat5Size())
                .fuelCharge(carrierAccount.getFuelCharge())
                .perKgCubic(carrierAccount.getPerKgCubic())
                .cartonCubic(carrierAccount.getCartonCubic())
                .cartonMaxH(carrierAccount.getCartonMaxH())
                .cartonMaxL(carrierAccount.getCartonMaxL())
                .cartonMaxW(carrierAccount.getCartonMaxW())
                .cartonMaxKg(carrierAccount.getCartonMaxKg())
                .palletCubic(carrierAccount.getPalletCubic())
                .palletMaxH(carrierAccount.getPalletMaxH())
                .palletMaxL(carrierAccount.getPalletMaxL())
                .palletMaxW(carrierAccount.getPalletMaxW())
                .palletMaxKg(carrierAccount.getPalletMaxKg())
                .cartonAverageWeight(carrierAccount.getCartonAverageWeight())
                .palletOvercharge(carrierAccount.getPalletOvercharge())
                .halfPalletMaxH(carrierAccount.getHalfPalletMaxH())
                .halfPalletMaxL(carrierAccount.getHalfPalletMaxL())
                .halfPalletMaxW(carrierAccount.getHalfPalletMaxW())
                .halfPalletMaxKg(carrierAccount.getHalfPalletMaxKg())
                .ruleCatFormat1(carrierAccount.getRuleCatFormat1())
                .ruleCatFormat2(carrierAccount.getRuleCatFormat2())
                .ruleCatFormat3(carrierAccount.getRuleCatFormat3())
                .ruleCatFormat4(carrierAccount.getRuleCatFormat4())
                .ruleCatFormat5(carrierAccount.getRuleCatFormat5())
                .build();
    }
}

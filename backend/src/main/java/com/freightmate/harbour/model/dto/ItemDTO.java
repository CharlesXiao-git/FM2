package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDTO {
    private long id;
    private long consignmentId;
    private Integer quantity;
    private long itemTypeId;
    private Float length;
    private Float width;
    private Float height;
    private Float weight;
    private Float totalWeight;
    private Float volume;
    private Boolean isHazardous;

    public static Item toItem(ItemDTO dto) {
        return Item.builder()
                .id(dto.id)
                .consignmentId(dto.consignmentId)
                .quantity(dto.quantity)
                .itemTypeId(dto.itemTypeId)
                .length(dto.length)
                .width(dto.width)
                .height(dto.height)
                .weight(dto.weight)
                .totalWeight(dto.totalWeight)
                .volume(dto.volume)
                .isHazardous(dto.isHazardous)
                .isDeleted(false)
                .build();
    }

    public static ItemDTO fromItem(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .consignmentId(item.getConsignmentId())
                .quantity(item.getQuantity())
                .itemTypeId(item.getItemTypeId())
                .length(item.getLength())
                .width(item.getWidth())
                .height(item.getHeight())
                .weight(item.getWeight())
                .totalWeight(item.getTotalWeight())
                .volume(item.getVolume())
                .isHazardous(item.getIsHazardous())
                .build();
    }

}

package com.freightmate.harbour.model.dto;

import com.freightmate.harbour.model.Suburb;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuburbDTO {

    private long id;
    private String name;
    private Integer postcode;
    private String state;
    private String country;

    public static Suburb toSuburb (SuburbDTO dto) {
        return Suburb.builder()
                .id(dto.id)
                .name(dto.name)
                .postcode(dto.postcode)
                .state(dto.state)
                .country(dto.country)
                .build();
    }

    public static SuburbDTO fromSuburb (Suburb suburb) {
        return SuburbDTO.builder()
                .id(suburb.getId())
                .name(suburb.getName())
                .postcode(suburb.getPostcode())
                .state(suburb.getState())
                .country(suburb.getCountry())
                .build();
    }
}

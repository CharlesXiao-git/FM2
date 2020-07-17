package com.freightmate.harbour.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuspostLocality {
    private String category;
    private String location;
    private Integer id;
    private Double latitude;
    private Double longditude;
    private Integer postcode;
    private String state;
}
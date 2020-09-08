package com.freightmate.harbour.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarrierZoneRaw {
    String postcode;
    String suburbTown;
    String carrierName;
    String zone;
}

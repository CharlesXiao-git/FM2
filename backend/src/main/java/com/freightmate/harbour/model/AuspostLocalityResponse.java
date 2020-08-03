package com.freightmate.harbour.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuspostLocalityResponse {
    @JsonAlias("localities")
    private AuspostLocalityWrapper localitiesWrapper;

}

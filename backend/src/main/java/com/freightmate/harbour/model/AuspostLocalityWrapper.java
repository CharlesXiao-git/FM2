package com.freightmate.harbour.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuspostLocalityWrapper {
    @JsonAlias("locality")
    private List<AuspostLocality> localities;
}
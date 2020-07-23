package com.freightmate.harbour.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuspostLocalityWrapper {
    @JsonAlias("locality")
    private List<AuspostLocality> localities;

    /**
     * Combine two sets of localities, and return a list of combined without duplicates
     * @param additionalLocalities the list to add to the wrappers list
     */
    public void extend (List<AuspostLocality> additionalLocalities){
        Set<AuspostLocality> set = new LinkedHashSet<>(this.localities);
        set.addAll(additionalLocalities);
        this.localities = new ArrayList<>(set);
    }
}
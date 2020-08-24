package com.freightmate.harbour.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuspostLocalityWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(AuspostLocalityWrapper.class);
    @JsonAlias("locality")
    private List<AuspostLocality> localities;

    public static AuspostLocalityWrapper fromJson(JsonNode node, ObjectMapper mapper) {
        try {
            // if we have a valid response
            if (node.has("localities")){

                // if we have a list of localities
                if(node.get("localities").has("locality") && node.get("localities").get("locality").isArray()){
                    // convert as expected
                    return mapper.treeToValue(node.get("localities"), AuspostLocalityWrapper.class);
                }

                // if only a single locality is returned, construct the required object from it
                AuspostLocality locality = mapper
                        .treeToValue(
                                node.get("localities").get("locality"),
                                AuspostLocality.class
                        );

                return new AuspostLocalityWrapper(Collections.singletonList(locality)) ;
            }
        } catch (JsonProcessingException e) {
            // return an empty obj and log if we cannot parse their response.
            LOG.error("Unable to parse address response: {}", node);

        }

        return new AuspostLocalityWrapper();
    }

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
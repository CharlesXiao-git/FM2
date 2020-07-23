package com.freightmate.harbour.controller;

import com.freightmate.harbour.model.AuspostLocalityWrapper;
import com.freightmate.harbour.service.PostCodeService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    @Autowired
    private PostCodeService postCodeService;

    /**
     * Lookup using locality string or postcode and combine results
     * @param postcode number in range 100-9999 for a Australian locality
     * @param locality String to search for eg "keysb"
     * @return list of matches, 204 when no results but lookup succeeded
     */
    @RequestMapping(path="/locality", method = RequestMethod.GET)
    public ResponseEntity<AuspostLocalityWrapper> fetchLocality(
            @Param("postcode") String postcode,
            @Param("locality") String locality){

        // no point calculating more than once.
        boolean isInvalidPostcode = postCodeService.isInvalidPostcode(postcode);

        // Make sure we have a valid request
        if(isInvalidPostcode && Strings.isBlank(locality)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        //  create our wrapper object to extend with result from both requests
        AuspostLocalityWrapper wrapper = new AuspostLocalityWrapper();

        try {
            // if we have a post code do a postcode lookup
            if (!isInvalidPostcode){
                AuspostLocalityWrapper postCodeResponse = postCodeService
                        .getLocality(postcode)
                        .getLocalitiesWrapper();

                // add any results we have to the wrapper to be returned
                wrapper.extend(postCodeResponse.getLocalities());
            }

            // if we have a locality string do a lookup
            if (!locality.isBlank()){
                AuspostLocalityWrapper localityResponse = postCodeService
                        .getLocality(locality)
                        .getLocalitiesWrapper();

                // add any results we have to the wrapper to be returned
                wrapper.extend(localityResponse.getLocalities());
            }
        } catch (HttpClientErrorException e) {
            // If something unexpected happens. Send a 500
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        // When we call auspost if we don't get any localities back lets send a 204
        if (Objects.isNull(wrapper.getLocalities()) || wrapper.getLocalities().isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return ResponseEntity.ok(wrapper);


    }
}

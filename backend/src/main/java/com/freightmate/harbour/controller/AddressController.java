package com.freightmate.harbour.controller;

import com.freightmate.harbour.model.AuspostLocalityWrapper;
import com.freightmate.harbour.service.PostCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    @Autowired
    private PostCodeService postCodeService;

    @RequestMapping(path="/postcode/{postcode}", method = RequestMethod.GET)
    public ResponseEntity<AuspostLocalityWrapper> fetchLocality(@PathVariable("postcode") String postcode){
        if(postCodeService.isInvalidPostcode(postcode)){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        try {
            return ResponseEntity
                    .ok(postCodeService
                            .getLocality(Integer.parseInt(postcode))
                            .getLocalitiesWrapper()
                    );
        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}

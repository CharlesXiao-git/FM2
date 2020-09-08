package com.freightmate.harbour.controller;

import com.freightmate.harbour.model.Carrier;
import com.freightmate.harbour.model.dto.CarrierDTO;
import com.freightmate.harbour.service.CarrierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/carrier")
public class CarrierController {

    @Autowired
    private CarrierService carrierService;

    /**
     * Carrier controller to get a list of carriers from the database
     *
     * @return list of carriers
     */
    @GetMapping
    public ResponseEntity<List<CarrierDTO>> searchAllCarriers()
    {
        List<Carrier> allCarriers = carrierService.getAllCarriers();

        if (allCarriers.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return ResponseEntity.ok(
                allCarriers
                .stream()
                .map(CarrierDTO::fromCarrier)
                .collect(Collectors.toList())
        );
    }

    /**
     * Carrier account create endpoint
     */
    @PostMapping(path="/account")
    public ResponseEntity<String> createCarrierAccount() {
        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    /**
     * Carrier account read endpoint
     */
    @GetMapping(path="/account")
    public ResponseEntity<String> readCarrierAccount() {
        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    /**
     * Carrier account update endpoint
     */
    @PutMapping(path="/account")
    public ResponseEntity<String> updateCarrierAccount() {
        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    /**
     * Carrier account delete endpoint
     */
    @DeleteMapping(path="/account")
    public ResponseEntity<String> deleteCarrierAccount() {
        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }
}

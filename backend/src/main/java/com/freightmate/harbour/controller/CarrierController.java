package com.freightmate.harbour.controller;

import com.freightmate.harbour.exception.ForbiddenException;
import com.freightmate.harbour.model.*;
import com.freightmate.harbour.model.dto.CarrierAccountDTO;
import com.freightmate.harbour.model.dto.CarrierDTO;
import com.freightmate.harbour.service.CarrierAccountService;
import com.freightmate.harbour.service.CarrierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/carrier")
public class CarrierController {

    @Autowired
    private CarrierService carrierService;

    @Autowired
    private CarrierAccountService carrierAccountService;

    private static final Logger LOG = LoggerFactory.getLogger(CarrierController.class);

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
     * Create Carrier Account Endpoint
     * This endpoint is to create the relationship record between the Broker, Customer and Carrier
     * @param carrierAccount    The JSON request body with the necessary details to create the CarrierAccount
     * @param authentication    Spring security authentication object to get the current session user info. Not needed
     *                          to pass in the API URL
     * @return The endpoint will return the view of the created CarrierAccount in JSON
     */
    @PostMapping(path="/account")
    public ResponseEntity<CarrierAccountDTO> createCarrierAccount(@RequestBody CarrierAccount carrierAccount,
                                                                  Authentication authentication) {
        // Get the username of the requestor
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        // Validate that user is a broker
        if (!authToken.getRole().equals(UserRole.BROKER)) {
            throw new ForbiddenException("User does not have permission to access create carrier account endpoint.");
        }

        try {
            return ResponseEntity.ok(
                    CarrierAccountDTO.fromCarrierAccount(
                            carrierAccountService.createCarrierAccount(
                                    carrierAccount,
                                    authToken.getUserId()
                            )
                    )
            );
        }catch (DataAccessException e) {
            LOG.error("Unable to create carrier account: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Read Carrier Account Endpoint
     * This endpoint is to get the list of CarrierAccount associated for the user
     * Currently this endpoint only accept Broker user
     * @param authentication    Spring security authentication object to get the current session user info. Not needed
     *                          to pass in the API URL
     * @return The endpoint will return a list of CarrierAccount records associated for the broker
     */
    @GetMapping(path="/account")
    public ResponseEntity<List<CarrierAccountDTO>> readCarrierAccount(Authentication authentication) {
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        // TODO: may need to remove this in the future when we can use this endpoint for other user as well
        // Validate that user is a broker
        if (!authToken.getRole().equals(UserRole.BROKER)) {
            throw new ForbiddenException("User does not have permission to access read carrier account endpoint.");
        }

        try {
            List<CarrierAccount> carrierAccounts = carrierAccountService.readCarrierAccount(authToken.getUserId());

            return ResponseEntity.ok(
                    carrierAccounts
                            .stream()
                            .map(CarrierAccountDTO::fromCarrierAccount)
                            .collect(Collectors.toList())
            );
        } catch (DataAccessException e) {
            LOG.error("Unable to read carrier account: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Update Carrier Account Endpoint
     * @param carrierAccountDTO     The JSON request body to update the existing CarrierAccount
     * @param authentication        Spring security authentication object to get the current session user info. Not needed
     *                              to pass in the API URL
     * @return It will return a HttpStatus.NO_CONTENT (204) when it has successfully perform the update.
     * Note: Nothing to be updated will also return a 204
     */
    @PutMapping(path="/account")
    public ResponseEntity<String> updateCarrierAccount(@RequestBody CarrierAccountDTO carrierAccountDTO,
                                                       Authentication authentication) {
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        // Validate that user is a broker
        if (!authToken.getRole().equals(UserRole.BROKER)) {
            throw new ForbiddenException("User does not have permission to access update carrier account endpoint.");
        }

        try {
            carrierAccountService.updateCarrierAccount(carrierAccountDTO, authToken.getUserId());
            // Return 204 after successful update
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (DataAccessException e) {
            LOG.error("Unable to update carrier account: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Delete Carrier Account Endpoint
     * @param ids               This is the list of CarrierAccount.id that belongs to the Broker to be deleted.
     * @param authentication    Spring security authentication object to get the current session user info. Not needed
     *                          to pass in the API URL
     * @return It will return a HttpStatus.NO_CONTENT (204) when it has successfully perform the delete.
     * Note: Nothing to be delete will also return a 204
     */
    @DeleteMapping(path="/account")
    public ResponseEntity<String> deleteCarrierAccount(@RequestParam("ids") List<Long> ids,
                                                       Authentication authentication) {
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        // Validate that user is a broker
        if (!authToken.getRole().equals(UserRole.BROKER)) {
            throw new ForbiddenException("User does not have permission to access delete carrier account endpoint.");
        }

        try {
            carrierAccountService.deleteCarrierAccount(ids, authToken.getUserId());

            // return 204 after successful delete
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (DataAccessException e) {
            LOG.error("Unable to delete the carrier account: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}

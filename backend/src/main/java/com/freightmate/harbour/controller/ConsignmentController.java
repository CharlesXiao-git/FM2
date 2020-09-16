package com.freightmate.harbour.controller;

import com.freightmate.harbour.exception.ForbiddenException;
import com.freightmate.harbour.model.*;
import com.freightmate.harbour.model.dto.ConsignmentDTO;
import com.freightmate.harbour.repository.OfferRepository;
import com.freightmate.harbour.service.ConsignmentService;
import com.freightmate.harbour.service.ItemTypeService;
import com.freightmate.harbour.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/consignment")
public class ConsignmentController {

    @Autowired
    private ConsignmentService consignmentService;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(ConsignmentController.class);

    /**
     * Create Consignment Endpoint
     * @param consignmentRequest    The JSON request body to create the Consignment object
     * @param authentication        Spring security authentication object to get the current session user info. Not needed
     *                              to pass in the API URL
     * @return This endpoint will return a view of the created Consignment object in JSON
     */
    @PostMapping
    public ResponseEntity<ConsignmentDTO> createConsignment(@RequestBody Consignment consignmentRequest,
                                                            Authentication authentication) {
        // Get the username of the requestor
        long userId = ((AuthToken) authentication.getPrincipal()).getUserId();

        // Check that the consignment Owner ID belongs to the logged in user
        if(consignmentRequest.getUserClientId() != 0 &&
                userId != consignmentRequest.getUserClientId() &&
                !userService.isChildOf(userId, consignmentRequest.getUserClientId())
        ) {
            throw new ForbiddenException("User does not have permission to create a consignment for the provided owner");
        }

        try {
            return ResponseEntity.ok(
                    ConsignmentDTO.fromConsignment(
                            consignmentService.createConsignment(
                                    consignmentRequest,
                                    (consignmentRequest.getUserClientId() != 0 ? consignmentRequest.getUserClientId() : userId)
                            )
                    )
            );
        }catch (DataAccessException e) {
            LOG.error("Unable to create consignment: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Read Consignment Endpoint
     * @param pageable          Required for pagination. This can be provided optionally via the API URL with `page` and
     *                          `size` attributes. `page` is the page index number that you want to display. `size` is the
     *                          number of records to be displayed for that page.
     *                          Not providing the attributes will result all records to be retrieved and displayed.
     * @param ownerId           This field is optional. Providing a valid user ID will allow the endpoint to get a list
     *                          of consignments that belongs to that user. The endpoint will also perform a validation
     *                          if the logged in user is the parent of the provided user ID
     * @param authentication    Spring security authentication object to get the current session user info. Not needed
     *                          to pass in the API URL
     * @return This endpoint will return a list of consignments
     */
    @GetMapping
    public ResponseEntity<ConsignmentQueryResult> readConsignment(Pageable pageable,
                                                  @RequestParam Optional<Long> ownerId,
                                                  Authentication authentication) {
        // Get the username of the requestor
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        // Check that the consignment Owner ID belongs to the logged in user
        if(ownerId.isPresent() &&
                authToken.getUserId() != ownerId.get() &&
                !userService.isChildOf(authToken.getUserId(), ownerId.get())
        ) {
            throw new ForbiddenException("User does not have permission to read consignment");
        }

        try {
            List<Consignment> con = consignmentService.readConsignment(
                    (ownerId.orElseGet(authToken::getUserId)),
                    (ownerId.isPresent() ? UserRole.CLIENT : authToken.getRole()),
                    pageable);

            return ResponseEntity.ok(
                    ConsignmentQueryResult
                            .builder()
                            .consignments(
                                con
                                    .stream()
                                    .map(ConsignmentDTO::fromConsignment)
                                    .collect(Collectors.toList())
                            )
                            .count(con.size())
                            .build()
            );
        } catch (DataAccessException e) {
            LOG.error("Unable to read consignment: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Update Consignment Endpoint
     * @param consignmentRequest    The JSON request body that has the updated details
     * @param authentication        Spring security authentication object to get the current session user info. Not needed
     *                              to pass in the API URL
     * @return This endpoint will return HttpStatus.NO_CONTENT (204) upon successful update
     * NOTE: If there is nothing to be updated, this endpoint will return 204 as well
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity updateConsignment(@RequestBody ConsignmentDTO consignmentRequest,
                                                            Authentication authentication) {
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        try {
            consignmentService.updateConsignment(consignmentRequest, authToken.getRole(), authToken.getUserId());
            // Return 204 after successful update
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (DataAccessException e) {
            LOG.error("Unable to update consignment: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Delete Consignment Endpoint
     * @param ids               This is a list of consignment IDs to be deleted. The endpoint will also validate if the
     *                          IDs belong to the logged in user
     * @param authentication    Spring security authentication object to get the current session user info. Not needed
     *                          to pass in the API URL
     * @return This endpoint will return HttpStatus.NO_CONTENT (204) upon successful delete
     * NOTE: If there is nothing to be deleted, this endpoint will return 204 as well
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteConsignment(@RequestParam("ids") List<Long> ids,
                                                              Authentication authentication) {
        // Get the username of the requestor
        long userId = ((AuthToken) authentication.getPrincipal()).getUserId();

        try {
            consignmentService.deleteConsignment(ids, userId);
            // return 204 after successful delete
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (DataAccessException e) {
            LOG.error("Unable to delete consignment: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    /**
     * Read Item Type Endpoint
     * This endpoint is to get a list of item types. Example usage is with the Consignment form when adding an item
     * Each item will have a dropdown to identify the item type. This endpoint is to populate the list of that dropdown
     */
    @RequestMapping(path="/itemTypes", method = RequestMethod.GET)
    public ResponseEntity<List<ItemType>> retrieveItemType() {
        try {
            return ResponseEntity.ok(
                    itemTypeService.getItemTypes()
            );
        } catch (DataAccessException e) {
            LOG.error("Unable to retrieve item type list: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    // todo public ResponseEntity<List<Offer>> calculateOffers(@RequestBody Consignment consignmentRequest, Authentication authentication)
    @GetMapping(path = "/offers")
    public ResponseEntity<List<Offer>> calculateOffers() {

        // Add a dummy consignment
        // send the consignment to get the price from the carrier FTP and API endpoint and store the offers in the database
        // send the consignment id to the offer query

        // This is just returning mock offers for now
        try {
            return ResponseEntity.ok(
                    offerRepository.getOffers()
            );
        } catch (DataAccessException e) {
            LOG.error("Unable to retrieve offers: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    // This is just a mock for selecting the offer for the consignment. This is to be removed.
    @PostMapping(path = "/offers")
    public ResponseEntity setSelectedOffer(long offerId, long consignmentId) {

        try {
            consignmentService.createOffer(offerId, consignmentId);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();

        } catch (DataAccessException e) {
            LOG.error("Unable to retrieve offers: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}

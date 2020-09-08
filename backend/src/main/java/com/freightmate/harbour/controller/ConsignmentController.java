package com.freightmate.harbour.controller;

import com.freightmate.harbour.exception.ForbiddenException;
import com.freightmate.harbour.model.*;
import com.freightmate.harbour.model.dto.ConsignmentDTO;
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
    private UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(ConsignmentController.class);

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
}

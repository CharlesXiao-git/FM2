package com.freightmate.harbour.controller;

import com.freightmate.harbour.model.AuthToken;
import com.freightmate.harbour.model.Consignment;
import com.freightmate.harbour.model.ConsignmentQueryResult;
import com.freightmate.harbour.model.ItemType;
import com.freightmate.harbour.model.dto.ConsignmentDTO;
import com.freightmate.harbour.repository.ItemTypeRepository;
import com.freightmate.harbour.service.ConsignmentService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/consignment")
public class ConsignmentController {

    @Autowired
    private ConsignmentService consignmentService;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    private static final Logger LOG = LoggerFactory.getLogger(ConsignmentController.class);

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ConsignmentDTO> createConsignment(@RequestBody Consignment consignmentRequest,
                                                            Authentication authentication) {
        // Get the username of the requestor
        String username = ((AuthToken) authentication.getPrincipal()).getUsername();

        try {
            return ResponseEntity.ok(
                    ConsignmentDTO.fromConsignment(
                            consignmentService.createConsignment(consignmentRequest, username)
                    )
            );
        }catch (DataAccessException e) {
            LOG.error("Unable to create consignment: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ConsignmentQueryResult> readConsignment(Pageable pageable,
                                                  Authentication authentication) {
        // Get the username of the requestor
        long userId = ((AuthToken) authentication.getPrincipal()).getUserId();

        try {
            List<Consignment> con = consignmentService.readConsignment(userId,pageable);

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
    public ResponseEntity<List<ItemType>> retrieveItemType(@RequestParam("isCustom") Boolean isCustom) {
        try {
            return ResponseEntity.ok(
                    itemTypeRepository.getItemTypes(isCustom)
            );
        } catch (DataAccessException e) {
            LOG.error("Unable to retrieve item type list: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}

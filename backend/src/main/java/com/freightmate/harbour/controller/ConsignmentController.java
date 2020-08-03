package com.freightmate.harbour.controller;

import com.freightmate.harbour.model.Consignment;
import com.freightmate.harbour.model.ConsignmentQueryResult;
import com.freightmate.harbour.model.dto.ConsignmentDto;
import com.freightmate.harbour.service.ConsignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/consignment")
public class ConsignmentController {

    @Autowired
    private ConsignmentService consignmentService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Consignment> createConsignment(@RequestBody Consignment consignmentRequest,
                                                    Authentication authentication) {
        // Get the username of the requestor
        String username = (String) authentication.getPrincipal();

        try {
            Consignment result = consignmentService.createConsignment(consignmentRequest, username);
            // Return 204 if result is null
            if(Objects.isNull(result)) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }

            return ResponseEntity.ok(result);
        }catch (DataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ConsignmentQueryResult> readConsignment(Pageable pageable,
                                                  Authentication authentication) {
        // Get the username of the requestor
        String username = (String) authentication.getPrincipal();

        try {
            return ResponseEntity.ok(
                    consignmentService
                            .readConsignment(
                                    username,
                                    pageable
                            )
            );
        } catch (DataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Consignment> updateConsignment(@RequestBody ConsignmentDto consignmentRequest,
                                                         Authentication authentication) {
        String requestorUsername = (String) authentication.getPrincipal();

        try {
            consignmentService.updateConsignment(consignmentRequest, requestorUsername);
            // Return 204 after successful update
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (DataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteConsignment(@RequestParam("ids") List<Long> ids,
                                                    Authentication authentication) {
        // Get the username of the requestor
        String username = (String) authentication.getPrincipal();

        try {
            consignmentService.deleteConsignment(ids, username);
            // return 204 after successful delete
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (DataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}

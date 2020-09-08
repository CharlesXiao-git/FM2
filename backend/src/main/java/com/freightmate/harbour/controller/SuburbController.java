package com.freightmate.harbour.controller;

import com.freightmate.harbour.helper.CSVHelper;
import com.freightmate.harbour.model.AuthToken;
import com.freightmate.harbour.model.FileUploadRequest;
import com.freightmate.harbour.model.Suburb;
import com.freightmate.harbour.service.SuburbService;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/v1/suburb")
public class SuburbController {

    private static final Logger LOG = LoggerFactory.getLogger(SuburbController.class);
    private final SuburbService suburbService;

    SuburbController(@Autowired SuburbService suburbService){
        this.suburbService = suburbService;
    }

    /**
     * Accepts a file upload request containing the zones csv (same format as src/test/resources/zonesSmall.csv)
     * Parses the file, and creates a Suburb for each suburb/postcode pair
     * This process upserts rows, so it will add any new suburb/postcode pair, or update any existing to the
     * provided value.
     *
     * The process executes in batches, this is as of August 2020, there are ~20,000 Suburb/postcode pairs
     * @param request FileUploadRequest containing the file and the metadata for the file upload
     * @param authentication The authentication token of the logged in user
     * @return the number
     */
    @PostMapping
    public ResponseEntity<Integer> importZones(@RequestBody FileUploadRequest request,
                                               Authentication authentication){

        // requestor user id
        long userId = ((AuthToken) authentication.getPrincipal()).getUserId();

        // long term this should be done using spring for all requests with an intercepter
        LocalDateTime start = LocalDateTime.now();

        if (request.getContent().length == 0 || !request.getContentType().equals("text/csv")){
            return ResponseEntity.badRequest().build();
        };

        try {
            // Convert the csv content to a list of csv records to be used
            List<CSVRecord> records = CSVHelper.recordsFromBytes(request.getContent());

            // convert the records to suburbs
            List<Suburb> suburbs = suburbService.parseSuburbs(records);

            //upsert the suburbs in batches
            Integer suburbUpsertCount = suburbService.batchUpsertSuburbs(suburbs, userId);

            LocalDateTime end = LocalDateTime.now();
            LOG.info("Upserted  {} Suburb records in: {}ms", suburbUpsertCount, start.until( end, ChronoUnit.MILLIS ));

            return ResponseEntity.ok(suburbUpsertCount);

        } catch (IOException e) {
            LOG.error("Unable to parse csv file: ", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        } catch (DataAccessException e) {
            LOG.error("Unable to upsert suburbs: ", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}

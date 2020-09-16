package com.freightmate.harbour.controller;

import com.freightmate.harbour.model.AuthToken;
import com.freightmate.harbour.model.Manifest;
import com.freightmate.harbour.model.ManifestQueryResult;
import com.freightmate.harbour.model.dto.ManifestDTO;
import com.freightmate.harbour.service.ManifestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/manifest")
public class ManifestController {

    @Autowired
    private ManifestService manifestService;

    @GetMapping
    public ResponseEntity<ManifestQueryResult> readManifest(Pageable pageable,
                                                            Authentication authentication) {
        // Get the username of the requestor
        AuthToken authToken = (AuthToken) authentication.getPrincipal();

        try {
            List<Manifest> manifests = manifestService.readManifest(
                    authToken.getUserId(),
                    authToken.getRole(),
                    pageable);

            return ResponseEntity.ok(
                    ManifestQueryResult
                            .builder()
                            .manifests(
                                    manifests
                                            .stream()
                                            .map(ManifestDTO::fromManifest)
                                            .collect(Collectors.toList())
                            )
                            .count(manifests.size())
                            .build()
            );
        } catch (DataAccessException e) {
            log.error("Unable to read manifest: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}

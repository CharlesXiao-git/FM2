package com.freightmate.harbour.service;

import com.freightmate.harbour.model.Manifest;
import com.freightmate.harbour.model.UserRole;
import com.freightmate.harbour.repository.ManifestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManifestService {

    private final ManifestRepository manifestRepository;

    ManifestService(@Autowired ManifestRepository manifestRepository) {
        this.manifestRepository = manifestRepository;
    }

    // Read
    public List<Manifest> readManifest(long userId, UserRole userRole, Pageable pageable) {
        // Find manifests under the user
        return manifestRepository.findManifests(userRole.name(), userId, pageable);
    }
}

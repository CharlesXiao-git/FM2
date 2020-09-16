package com.freightmate.harbour.model;

import com.freightmate.harbour.model.dto.ManifestDTO;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ManifestQueryResult {
    int count;
    List<ManifestDTO> manifests;
}

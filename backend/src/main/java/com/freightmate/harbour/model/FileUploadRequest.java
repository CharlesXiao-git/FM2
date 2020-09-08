package com.freightmate.harbour.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileUploadRequest {
    byte[] content;
    String filename;
    @JsonProperty("Content-Type")
    String contentType;
}

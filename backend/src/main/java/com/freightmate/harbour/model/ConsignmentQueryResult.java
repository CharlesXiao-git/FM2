package com.freightmate.harbour.model;

import com.freightmate.harbour.model.dto.ConsignmentDTO;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ConsignmentQueryResult {
    int count;
    List<ConsignmentDTO> consignments;
}

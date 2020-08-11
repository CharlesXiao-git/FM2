package com.freightmate.harbour.model;

import com.freightmate.harbour.model.dto.AddressDTO;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AddressQueryResult {
    int count;
    List<AddressDTO> addresses;
}

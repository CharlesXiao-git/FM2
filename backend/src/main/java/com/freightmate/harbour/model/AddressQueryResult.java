package com.freightmate.harbour.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AddressQueryResult {
    int count;
    List<Address> addresses;
}

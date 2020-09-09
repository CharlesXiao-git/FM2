package com.freightmate.harbour.model;

import org.junit.Test;

import java.util.Objects;

public class AddressTest {

    // A very simple test
    @Test
    public void AddressBookShouldSucceed_WhenConstructingNewAddress() {
        Suburb suburb = Suburb.builder()
                .id(1)
                .name("SOUTHBANK")
                .postcode(3006)
                .state("VIC")
                .country("AUSTRALIA")
                .build();

        Address newAddress = Address.builder()
                .company("Smithsonian")
                .suburb(suburb)
                .contactName("Mr. Smith")
                .build();

        assert Objects.nonNull(newAddress);
    }
}
